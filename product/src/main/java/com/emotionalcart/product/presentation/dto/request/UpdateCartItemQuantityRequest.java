package com.emotionalcart.product.presentation.dto.request;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemQuantityRequest {

    // 장바구니 아이템 아이디
    private String itemId;

    // 상품 상세 옵션 수량
    private int optionDetailQuantity;

}
