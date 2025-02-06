package com.emotionalcart.order.infra.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPriceRequest {

    /**
     * 상품 아이디
     */
    private Long productId;

    /**
     * 상품 옵션
     */
    private List<ProductOption> productOptions;

    public static ProductPriceRequest of(@NotNull(message = "상품을 선택해주세요.") Long productId) {
        ProductPriceRequest request = new ProductPriceRequest();
        request.productId = productId;
        return request;
    }

    public void addOption(@NotNull(message = "상품 옵션을 선택해주세요.") Long productOptionId,
                          @NotNull(message = "상품 옵션 상세를 선택해주세요.") Long productOptionDetailId) {
        if (this.productOptions == null) {
            this.productOptions = new ArrayList<>();
        }
        this.productOptions.add(new ProductOption(productOptionId, productOptionDetailId));
    }

    @Getter
    @AllArgsConstructor
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

    }

}
