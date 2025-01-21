package com.emotionalcart.order.infra.product.dto;

import java.util.List;

/**
 * 상품 재고 요청
 */
public class ProductStockRequest {

    private Long productId;

    private List<ProductOption> productOptions;

    public static class ProductOption {

        /**
         * 상품 옵션 아이디
         */
        private Long productOptionId;

        /**
         * 상품 옵션 상세 아이디
         */
        private Long productOptionDetailId;

        /**
         * 수량
         */
        private int quantity;

    }

}
