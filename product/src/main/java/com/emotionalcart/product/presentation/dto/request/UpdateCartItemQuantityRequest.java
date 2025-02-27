package com.emotionalcart.product.presentation.dto.request;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemQuantityRequest {
    // 상품 아이디
    private Long productId;

    // 상품 옵션 및 상품 상세 옵션 맵 배열
    private List<Map<Long, Long>> optionDetailIdMapList;

    // 상품 상세 옵션 수량
    private int optionDetailQuantity;

}
