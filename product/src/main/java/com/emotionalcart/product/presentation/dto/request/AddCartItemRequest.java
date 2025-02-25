package com.emotionalcart.product.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequest {

    // 상품 정보
    private Long productId;
    private String productName;
    private int price;
    private int subTotalPrice;

    // 상품 옵션 정보
    private Long optionId;
    private String optionName;

    // 상품 상세 옵션 정보
    private Long optionDetailId;
    private String optionDetailValue;
    private int optionDetailQuantity;
    private int optionDetailAdditionalPrice;

    // 상품 이미지 정보
    private Long imageId;
    private String imageUrl;

    // 공급자 정보
    private Long providerId;
    private String providerName;

}
