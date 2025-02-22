package com.emotionalcart.product.infrastructure.stock;

import com.emotionalcart.product.infrastructure.stock.dto.StockQuantitySearchRequest;
import com.emotionalcart.product.infrastructure.stock.dto.StockQuantityValidateRequest;
import com.emotionalcart.product.infrastructure.stock.http.StockFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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
        Long productId = stockQuantitySearchRequest.getProductId();
        List<Long> optionIds = stockQuantitySearchRequest.getOptionDetailsIds();
        return stockFeignClient.getStockQuantity(productId, optionIds);
    }

    /**
     * 상품 재고 검증
     *
     * @param stockQuantityValidateRequest 상품 재고 검증 요청
     */
    public ResponseEntity<Boolean> validateStock(StockQuantityValidateRequest stockQuantityValidateRequest) {
        return stockFeignClient.getStockQuantities(stockQuantityValidateRequest);
    }
}
