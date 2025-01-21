package com.emotionalcart.order.infra.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductValidationRequest {

    /**
     * 상품 아이디
     */
    private Long productId;

    /**
     * 상품 옵션
     */
    private List<ProductOption> productOptions;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
