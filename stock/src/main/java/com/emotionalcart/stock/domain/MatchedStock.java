package com.emotionalcart.stock.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class MatchedStock {

    /**
     * 재고 식별자
     */
    private Long stockId;

    /**
     * 상품 상세 식별자
     */
    private List<Long> optionDetailsId;

    /**
     * 수량
     */
    private Integer quantity;

}
