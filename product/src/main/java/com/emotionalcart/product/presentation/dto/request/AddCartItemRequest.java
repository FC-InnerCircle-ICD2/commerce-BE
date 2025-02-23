package com.emotionalcart.product.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequest {
    // 상품 정보
    private Long productId;
    private String productName;
    private int productPrice;

    // 상품 옵션 정보
    private Long optionId;
    private String optionName;

    // 상품 상세 옵션
    private Long detailOptionId;
    private String detailOptionValue;
    private int detailOptionQuantity;

    // 상품 대표 이미지
    private Long imageId;
    private String imageUrl;

    // 카테고리
    private Long categoryId;
    private String categoryName;

    // 공급자
    private Long providerId;
    private String providerName;
}
