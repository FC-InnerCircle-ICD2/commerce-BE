package com.emotionalcart.product.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetail {
    private Long productId;
    private Long providerId;
    private String providerName;
    private Integer productPrice;
    private Long productOptionId;
    private String productOptionName;
    private Long productOptionDetailId;
    private String productOptionDetailName;
    private Integer productAdditionalPrice;

    public ProductDetail(Long productId,
                         Long providerId,
                         String providerName,
                         Integer price,
                         Long productOptionId,
                         String productOptionName,
                         Long productOptionDetailId,
                         String productOptionDetailName,
                         Integer productAdditionalPrice
    ) {
        this.productId = productId;
        this.providerId = providerId;
        this.providerName = providerName;
        this.productPrice = price;
        this.productOptionId = productOptionId;
        this.productOptionName = productOptionName;
        this.productOptionDetailId = productOptionDetailId;
        this.productOptionDetailName = productOptionDetailName;
        this.productAdditionalPrice = productAdditionalPrice;
    }
}

