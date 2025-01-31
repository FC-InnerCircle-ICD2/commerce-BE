package com.emotionalcart.order.domain.dto;

import lombok.Getter;

@Getter
public class BestSellingProduct {

    /**
     * 상품 아이디
     */
    private Long productId;

    /**
     * 카테고리 식별자
     * 카테고리 식별자는 최하위 자식 카테고리만 저장
     */
    private Long categoryId;

    /**
     * 주문 건 수
     */
    private Long totalOrder;

    /**
     * 총 판매 횟 수
     */
    private Long totalQuantitySold;

}
