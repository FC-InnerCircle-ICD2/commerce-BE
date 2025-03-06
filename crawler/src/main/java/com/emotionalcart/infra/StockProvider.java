package com.emotionalcart.infra;

import com.emotionalcart.domain.entity.Product;
import com.emotionalcart.domain.entity.ProductOptionDetail;
import com.emotionalcart.domain.entity.Stock;
import com.emotionalcart.domain.entity.StockOption;
import com.emotionalcart.infra.repository.ProductRepository;
import com.emotionalcart.infra.repository.StockRepository;
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
public class StockProvider {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void generateOptionCombinations(Long productId) {
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
                return;
            }
            saveNewStock(product, combinations);
            return;
        }
        log.warn("상품이 존재하지 않습니다. 상품 식별자: {}", productId);
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

    public void saveNewStock(Product product, List<List<Long>> combinations) {
        List<List<Long>> newCombinations = getNewCombinations(product.getId(), combinations);
        for (List<Long> newCombo : newCombinations) {
            Stock stock = Stock.of(product, 0); // 기본 재고 0으로 설정
            newCombo.stream()
                .map(optionDetailId -> StockOption.of(stock, ProductOptionDetail.of(optionDetailId)))
                .forEach(stock::addOption);
            stockRepository.save(stock);
        }
        log.info("재고 조합 생성 및 저장 완료 - 상품 식별자: {}, 신규 조합 : {}", product.getId(), newCombinations);

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
            .map(list ->
                     list.stream().sorted(Comparator.comparing(StockOption::getId))
                         .map(l -> String.valueOf(l.getId()))
                         .collect(Collectors.joining(","))) // 정렬 후 문자열 변환
            .collect(Collectors.toMap(Function.identity(), v -> true));

        return combinations.stream()
            .filter(combo -> !existingCombinationMap.containsKey(
                combo.stream().sorted().map(String::valueOf).collect(Collectors.joining(",")))) // 동일한 방식으로 비교
            .toList();
    }

}