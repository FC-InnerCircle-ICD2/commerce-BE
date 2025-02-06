package com.emotionalcart.order.infra.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    public static ProductValidationRequest of(@NotNull(message = "상품을 선택해주세요.") Long productId) {
        ProductValidationRequest request = new ProductValidationRequest();
        request.productId = productId;
        return request;
    }

    public void addOption(@NotNull(message = "상품 옵션을 선택해주세요.") Long productOptionId,
                          @NotNull(message = "상품 옵션 상세를 선택해주세요.") Long productOptionDetailId,
                          int quantity) {
        if (CollectionUtils.isEmpty(this.productOptions)) {
            this.productOptions = new ArrayList<>();
        }
        this.productOptions.add(ProductOption.of(productOptionId, productOptionDetailId, quantity));
    }

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

        public static ProductOption of(@NotNull(message = "상품 옵션을 선택해주세요.") Long productOptionId,
                                       @NotNull(message = "상품 옵션 상세를 선택해주세요.") Long productOptionDetailId,
                                       int quantity) {
            ProductOption productOption = new ProductOption();
            productOption.productOptionId = productOptionId;
            productOption.productOptionDetailId = productOptionDetailId;
            productOption.quantity = quantity;
            return productOption;
        }

    }

}
