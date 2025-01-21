package com.emotionalcart.product.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetail {
    private Long productId;
    private Long productOptionId;
    private boolean isRequired;
    private Long productOptionDetailId;
    private Integer quantity;

    public ProductDetail(Long productId, Long productOptionId, Boolean isRequired, Long productOptionDetailId, Integer quantity) {
        this.productId = productId;
        this.productOptionId = productOptionId;
        this.isRequired = isRequired;
        this.productOptionDetailId = productOptionDetailId;
        this.quantity = quantity;
    }
}

