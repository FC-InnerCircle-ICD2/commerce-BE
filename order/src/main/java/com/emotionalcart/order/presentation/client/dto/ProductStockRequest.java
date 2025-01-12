package com.emotionalcart.order.presentation.client.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 재고 요청
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockRequest {

    /**
     * 상품 요청 값들
     */
    private List<Product> products;

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Product {

        /**
         * 상품 아이디
         */
        private Long productId;

        /**
         * 상품 옵션 아이디
         */
        private Long productOptionId;

    }

}
