package com.emotionalcart.order.presentation.client.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 상품 재고 응답 값
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockResponse {

    /**
     * 상품 아이디
     */
    private Long productId;

    /**
     * 상품 옵션 아이디
     */
    private Long productOptionId;

    /**
     * 재고
     */
    private int quantity;

}
