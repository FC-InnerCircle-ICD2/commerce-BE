package com.emotionalcart.product.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOptionDetailWithImages {
    private Long OptionDetailId;
    private Long OptionId;
    private String value;
    private Integer quantity;
    private Integer additionalPrice;
    private Long imageId = null;
    private Integer fileOrder = null;
    private String url = null;

    public ProductOptionDetailWithImages(Long optionDetailId,
                                         Long optionId,
                                         String value,
                                         Integer quantity,
                                         Integer additionalPrice,
                                         Long imageId,
                                         Integer fileOrder,
                                         String url) {
        this.OptionDetailId = optionDetailId;
        this.OptionId = optionId;
        this.value = value;
        this.quantity = quantity;
        this.additionalPrice = additionalPrice;
        this.imageId = imageId;
        this.fileOrder = fileOrder;
        this.url = url;
    }
}
