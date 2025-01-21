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

    public ProductPrice(ProductPriceResponse productPriceResponse) {
        this.productId = productPriceResponse.getProductId();
        this.price = productPriceResponse.getPrice();
        this.productOptionId = productPriceResponse.getProductOptionId();
        this.productOptionDetailId = productPriceResponse.getProductOptionDetailId();
        this.additionalPrice = productPriceResponse.getAdditionalPrice();
    }

    public static ProductPrice convert(ProductPriceResponse productPriceResponse) {
        return new ProductPrice(productPriceResponse);
    }

}
