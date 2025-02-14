package com.emotionalcart.product.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetail {
    private Long productId;
    private Integer productPrice;
    private Long productOptionId;
    private Long productOptionDetailId;
    private Integer productAdditionalPrice;
    private Integer quantity;

    public ProductDetail(Long productId,
                         Integer price,
                         Long productOptionId,
                         Long productOptionDetailId,
                         Integer productAdditionalPrice,
                         Integer quantity
    ) {
        this.productId = productId;
        this.productPrice = price;
        this.productOptionId = productOptionId;
        this.productOptionDetailId = productOptionDetailId;
        this.productAdditionalPrice = productAdditionalPrice;
        this.quantity = quantity;
    }
}

