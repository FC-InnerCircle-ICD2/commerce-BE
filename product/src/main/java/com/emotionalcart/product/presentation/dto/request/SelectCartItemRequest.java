package com.emotionalcart.product.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectCartItemRequest {
    private Long productId;
    private Long optionId;
    private Long optionDetailId;
}
