package com.emotionalcart.stock.application;

import com.emotionalcart.stock.domain.*;
import com.emotionalcart.stock.domain.repository.ProductRepository;
import com.emotionalcart.stock.domain.repository.StockOptionRepository;
import com.emotionalcart.stock.domain.repository.StockRepository;
import com.emotionalcart.stock.infra.advice.exceptions.NotExistsStockException;
import com.emotionalcart.stock.infra.advice.exceptions.OutOfStockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockCommandService {

    private final StockRepository stockRepository;
    private final StockOptionRepository stockOptionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public List<CreatedStock> generateOptionCombinationsAndSaveStock(Long productId) {
        log.info("재고 조합 생성 및 저장 - 상품 식별자: {}", productId);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            List<List<Long>> optionDetailIds = product.getProductOptions().stream()
                .map(option -> option.getProductOptionDetails().stream()
                    .map(ProductOptionDetail::getId)
                    .toList())
                .toList();
            List<List<Long>> combinations = cartesianProduct(optionDetailIds);
            if (CollectionUtils.isEmpty(combinations)) {
                log.warn("새로운 조합이 존재하지 않습니다. 상품 식별자: {}", productId);
                return List.of();
            }
            return saveNewStock(product, combinations);
        }
        log.warn("상품이 존재하지 않습니다. 상품 식별자: {}", productId);
        return List.of();
    }

    /**
     * 테카르트 곱을 구한다.
     *
     * @param optionDetailIds 상품 옵션 디테일 식별자 목록
     * @return 상품 옵션 디테일 식별자들의 조합 목록
     */
    private static List<List<Long>> cartesianProduct(List<List<Long>> optionDetailIds) {
        List<List<Long>> result = new ArrayList<>();
        cartesianProductHelper(optionDetailIds, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * <pre>
     * 넘겨진 옵션 상세 식별자들로 조합을 만든다.
     * 백트래킹을 사용하기 때문에 순서를 바꾸지 않고 딱 한 가지 방향의 조합만을 만든다.
     * </pre>
     *
     * @param optionDetailIds 상품 옵션 상세 식별자 목록
     * @param depth           현재 depth
     * @param current         현재 까지의 조합
     * @param result          결과 목록
     */
    private static void cartesianProductHelper(List<List<Long>> optionDetailIds, int depth, List<Long> current, List<List<Long>> result) {
        if (depth == optionDetailIds.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (Long item : optionDetailIds.get(depth)) {
            current.add(item);
            cartesianProductHelper(optionDetailIds, depth + 1, current, result);
            current.removeLast();
        }
    }

    public List<CreatedStock> saveNewStock(Product product, List<List<Long>> combinations) {
        List<List<Long>> newCombinations = getNewCombinations(product.getId(), combinations);
        List<CreatedStock> createdStocks = new ArrayList<>();
        for (List<Long> newCombo : newCombinations) {
            Stock stock = Stock.of(product, 0); // 기본 재고 0으로 설정
            List<StockOption> stockOptions = new ArrayList<>();
            newCombo.stream()
                .map(optionDetailId -> StockOption.of(stock, ProductOptionDetail.of(optionDetailId)))
                .forEach(i -> {
                    stock.addOption(i);
                    stockOptions.add(i);
                });
            stockRepository.save(stock);
            stockOptionRepository.saveAll(stockOptions);
            createdStocks.add(CreatedStock.of(product.getId(), stock.getId(), newCombo));
        }
        log.info("재고 조합 생성 및 저장 완료 - 상품 식별자: {}, 신규 조합 : {}", product.getId(), newCombinations);

        return createdStocks;
    }

    /**
     * 기존에 저장되어 있는 재고와 현재 조합을 비교해서 새로 들어갈 조합들만 추출한다.
     *
     * @param productId    제품 식별자
     * @param combinations 저장된 제품 옵션들의 조합 목록
     * @return 신규로 저장될 조합 목록
     */
    private List<List<Long>> getNewCombinations(Long productId, List<List<Long>> combinations) {
        Map<String, Boolean> existingCombinationMap = stockRepository.findByProduct_Id(productId).stream()
            .map(Stock::getStockOptions)
            .map(list -> list.stream().sorted(Comparator.comparing(StockOption::getId)).map(l -> String.valueOf(l.getId())).collect(
                Collectors.joining(","))) // 정렬 후 문자열 변환
            .collect(Collectors.toMap(Function.identity(), v -> true));

        return combinations.stream()
            .filter(combo -> !existingCombinationMap.containsKey(
                combo.stream().sorted().map(String::valueOf).collect(Collectors.joining(",")))) // 동일한 방식으로 비교
            .toList();
    }

    /**
     * <pre>
     * 재고 수량 변경
     * 상품 식별자와 옵션 디테일 식별자 목록으로 재고 수량을 변경한다.
     * </pre>
     *
     * @param productId       상품 식별자
     * @param optionDetailIds 옵션 디테일 식별자 목록
     * @param quantity        변경할 수량
     * @return 변경된 재고 정보
     */
    @Transactional
    public UpdatedStock updateStockQuantity(Long productId, List<Long> optionDetailIds, int quantity) {
        log.info("재고 수량 변경 - 상품 식별자: {}, 옵션 디테일 식별자: {}, 수량: {}", productId, optionDetailIds, quantity);
        Stock stock = stockRepository.getStockByOptionIds(StockQuantitySearchCondition.of(productId, optionDetailIds))
            .orElseThrow(NotExistsStockException::new);
        stock.changeQuantity(quantity);
        return UpdatedStock.of(stock.getId(), optionDetailIds, quantity);
    }

    public boolean validateStockQuantity(StockQuantityValidateCommand command) {
        List<StockQuantityOptionValidateCommand> optionValidateCommands = command.getOptionValidateCommands();
        for (StockQuantityOptionValidateCommand optionValidateCommand : optionValidateCommands) {
            Stock stock = stockRepository.getStockByOptionIds(StockQuantitySearchCondition.of(command.getProductId(),
                                                                                              optionValidateCommand.getOptionDetailsIds()))
                .orElseThrow(NotExistsStockException::new);
            if (stock.getQuantity() < optionValidateCommand.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * <pre>
     * 재고 차감
     * 상품 식별자와 옵션 디테일 식별자 목록으로 재고 수량을 차감한다.
     * </pre>
     *
     * @param productId       상품 식별자
     * @param optionDetailIds 옵션 디테일 식별자 목록
     * @param quantity        차감할 수량
     * @return 차감된 재고 정보
     */
    @Transactional
    public DeductedStockInfo deductStockQuantity(Long productId, List<Long> optionDetailIds, int quantity) {
        log.info("재고 차감 - 상품 식별자: {}, 옵션 디테일 식별자: {}, 수량: {}", productId, optionDetailIds, quantity);
        Stock stock = stockRepository.getStockByOptionIds(StockQuantitySearchCondition.of(productId, optionDetailIds))
            .orElseThrow(NotExistsStockException::new);
        int remainQuantity = stock.getQuantity() - quantity;
        if (remainQuantity < 0) {
            throw new OutOfStockException();
        }
        stock.minusQuantity(quantity);
        log.info("재고 차감 완료 - 상품 식별자: {}, 옵션 디테일 식별자: {}, 차감 수량: {}, 잔여 수량: {}", productId, optionDetailIds, quantity, remainQuantity);
        return DeductedStockInfo.of(stock.getId(), optionDetailIds, remainQuantity);
    }

}
