package com.emotionalcart.product.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemQuantityRequest {
    // 상품 정보
    private Long productId;

    // 상품 옵션 정보
    private Long optionId;

    // 상품 상세 옵션
    private Long detailOptionId;
    private int detailOptionQuantity;

}
