package com.emotionalcart.order.infra.stock;

import com.emotionalcart.order.infra.advice.exceptions.StockException;
import com.emotionalcart.order.infra.product.dto.ProductQuantityValidateRequest;
import com.emotionalcart.order.infra.product.dto.ProductStockRequest;
import com.emotionalcart.order.infra.stock.dto.StockQuantityOptionValidateRequest;
import com.emotionalcart.order.infra.stock.dto.StockQuantityUpdateRequest;
import com.emotionalcart.order.infra.stock.dto.StockQuantityValidateRequest;
import com.emotionalcart.order.infra.stock.http.StockFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockFeignClient stockFeignClient;

    //    private final StockDomainWebClient stockDomainWebClient;

    /**
     * 상품 수량 검증
     * stock > /quantity/validate api 호출
     *
     * @param productQuantityValidateRequests
     */
    public void isValidProductQuantity(List<ProductQuantityValidateRequest> productQuantityValidateRequests) {
        Map<Long, List<StockQuantityOptionValidateRequest>> validStockQuantityMap = new HashMap<>();
        for (ProductQuantityValidateRequest productQuantityValidateRequest : productQuantityValidateRequests) {
            List<StockQuantityOptionValidateRequest> orDefault =
                validStockQuantityMap.getOrDefault(productQuantityValidateRequest.getProductId(), new ArrayList<>());

            StockQuantityOptionValidateRequest stockQuantityOptionValidateRequest =
                StockQuantityOptionValidateRequest.of(productQuantityValidateRequest.getQuantity(),
                                                      productQuantityValidateRequest.getProductOptions().stream().map(
                                                          ProductQuantityValidateRequest.ProductOption::getProductOptionDetailId).toList());

            orDefault.add(stockQuantityOptionValidateRequest);
            validStockQuantityMap.put(productQuantityValidateRequest.getProductId(), orDefault);
        }

        for (Long productId : validStockQuantityMap.keySet()) {
            StockQuantityValidateRequest stockQuantityValidateRequest =
                StockQuantityValidateRequest.of(productId, validStockQuantityMap.get(productId));

            ResponseEntity<Boolean> booleanResponseEntity = stockFeignClient.validateProductQuantity(stockQuantityValidateRequest);
            Boolean isValidateQuantity = booleanResponseEntity.getBody();
            if (Boolean.FALSE.equals(isValidateQuantity)) {
                throw new StockException("주문 시 품절된 상품이 있습니다. 다시 주문해주세요");
            }
        }

    }

    /**
     * 상품 수량 차감
     * StockQuantityUpdateRequest
     */
    public void deductStockQuantity(List<ProductStockRequest> productStockRequests) {
        for (ProductStockRequest productStockRequest : productStockRequests) {
            Long productId = productStockRequest.getProductId();
            List<Long> optionDetailIds =
                productStockRequest.getProductOptions().stream().map(ProductStockRequest.ProductOption::getProductOptionDetailId).toList();
            int quantity = productStockRequest.getQuantity();

            StockQuantityUpdateRequest stockQuantityUpdateRequest = StockQuantityUpdateRequest.of(productId, optionDetailIds, quantity);
            stockFeignClient.deductStockQuantity(stockQuantityUpdateRequest);
        }
    }

}
