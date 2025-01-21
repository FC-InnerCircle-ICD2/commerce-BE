package com.emotionalcart.order.infra.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 가격 응답값
 */
@Getter
@NoArgsConstructor
public class ProductPriceResponse {

    /**
     * 상품 아이디
     */
    private Long productId;

    /**
     * 가격
     */
    private double price;

    /**
     * 상품 옵션 아이디
     */
    private Long productOptionId;

    /**
     * 상품 옵션 상세 아이디
     */
    private Long productOptionDetailId;

    /**
     * 추가 금액
     */
    private double additionalPrice;

}
