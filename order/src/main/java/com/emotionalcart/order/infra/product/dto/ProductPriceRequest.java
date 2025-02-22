package com.emotionalcart.order.infra.product.dto;

import jakarta.validation.constraints.Min;
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

    private double price;

    /**
     * 상품 옵션
     */
    private List<ProductOption> productOptions;

    public static ProductPriceRequest of(@NotNull(message = "상품을 선택해주세요.") Long productId,
                                         @Min(value = 100, message = "상품 금액은 100원 이상이어야 합니다.") double price) {
        ProductPriceRequest request = new ProductPriceRequest();
        request.productId = productId;
        request.price = price;
        return request;
    }

    public void addOption(@NotNull(message = "상품 옵션을 선택해주세요.") Long productOptionId,
                          @NotNull(message = "상품 옵션 상세를 선택해주세요.") Long productOptionDetailId,
                          double additionalPrice) {
        if (this.productOptions == null) {
            this.productOptions = new ArrayList<>();
        }
        this.productOptions.add(new ProductOption(productOptionId, productOptionDetailId, additionalPrice));
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

        private double additionalPrice;

    }

}
