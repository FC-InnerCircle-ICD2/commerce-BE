package com.emotionalcart.product.infrastructure.stock;

import com.emotionalcart.product.infrastructure.stock.dto.StockQuantitySearchRequest;
import com.emotionalcart.product.infrastructure.stock.dto.StockQuantityUpdateRequest;
import com.emotionalcart.product.infrastructure.stock.dto.StockQuantityValidateRequest;
import com.emotionalcart.product.infrastructure.stock.http.StockFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StockService {
    private final StockFeignClient stockFeignClient;

    /**
     * 상품 재고 조회
     *
     * @param stockQuantitySearchRequest 상품 재고 조회 요청
     */
    @GetMapping("/quantity")
    public Integer getStockQuantity(StockQuantitySearchRequest stockQuantitySearchRequest) {
        return stockFeignClient.getStockQuantity(stockQuantitySearchRequest);
    }

    /**
     * 상품 재고 검증
     *
     * @param stockQuantityValidateRequest 상품 재고 검증 요청
     */
    public ResponseEntity<Boolean> validateStock(StockQuantityValidateRequest stockQuantityValidateRequest) {
        return stockFeignClient.getStockQuantities(stockQuantityValidateRequest);
    }

    public void generateOptionCombinationsAndSaveStock(@PathVariable(value = "productId") Long productId) {
        stockFeignClient.generateOptionCombinationsAndSaveStock(productId);
    }

    public void updateStockQuantity(@RequestBody StockQuantityUpdateRequest stockQuantityUpdateRequest) {
        stockFeignClient.updateStockQuantity(stockQuantityUpdateRequest);
    }
}
