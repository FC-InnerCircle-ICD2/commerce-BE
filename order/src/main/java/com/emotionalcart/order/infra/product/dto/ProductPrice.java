package com.emotionalcart.order.infra.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPrice {

    /**
     * 상품 아이디
     */
    private Long productId;

    /**
     * 가격
     */
    private double price;

    private ProductOption productOptions;

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
         * 추가 금액
         */
        private double additionalPrice;

        private ProductOption(ProductPriceResponse.ProductOption productOption) {
            this.productOptionId = productOption.getProductOptionId();
            this.productOptionDetailId = productOption.getProductOptionDetailId();
            this.additionalPrice = productOption.getAdditionalPrice();
        }

        public static ProductOption from(ProductPriceResponse.ProductOption productOption) {
            return new ProductOption(productOption);
        }

    }

    public ProductPrice(ProductPriceResponse productPriceResponse) {
        this.productId = productPriceResponse.getProductId();
        this.price = productPriceResponse.getPrice();
        this.productOptions = ProductOption.from(productPriceResponse.getProductOptions());
    }

    public static ProductPrice convert(ProductPriceResponse productPriceResponse) {
        return new ProductPrice(productPriceResponse);
    }

}
