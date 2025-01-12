package com.emotionalcart.order.domain.stock.service;

import com.emotionalcart.order.domain.stock.ProductStock;
import com.emotionalcart.order.presentation.advice.exceptions.ProductStockException;
import com.emotionalcart.order.presentation.client.ProductFeignClient;
import com.emotionalcart.order.presentation.client.dto.ProductStockRequest;
import com.emotionalcart.order.presentation.client.dto.ProductStockResponse;
import com.emotionalcart.order.presentation.client.dto.UpdateStockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 상품 재고 서비스
 */
@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductFeignClient productFeignClient;

    /**
     * 상품 재고 조회
     *
     * @param productStockRequest
     * @return
     */
    public List<ProductStock> getStock(ProductStockRequest productStockRequest) {
        ResponseEntity<Object> productStock = productFeignClient.getProductStock(productStockRequest);
        validateResponse(productStock);

        List<ProductStockResponse> productStockResponse = (List<ProductStockResponse>)productStock.getBody();
        return productStockResponse.stream().map(ProductStock::convert).toList();
    }

    /**
     * response 유효성 검증
     *
     * @param productStock
     */
    private static void validateResponse(ResponseEntity<Object> productStock) {
        if (!productStock.getStatusCode().is2xxSuccessful()) {
            throw new ProductStockException(Objects.requireNonNull(productStock.getBody()));
        }

        List<ProductStockResponse> productStockResponse = (List<ProductStockResponse>)productStock.getBody();
        assert productStockResponse != null;
        if (productStockResponse.isEmpty()) {
            throw new ProductStockException(productStockResponse);
        }
    }

    /**
     * 상품 재고 변경
     *
     * @param updateStockRequest
     */
    public void updateStock(UpdateStockRequest updateStockRequest) {
        ResponseEntity<Object> updateProductStock = productFeignClient.updateProductStock(updateStockRequest);
        validateResponse(updateProductStock);
    }

}
