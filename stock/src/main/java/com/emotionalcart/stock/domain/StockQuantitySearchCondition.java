package com.emotionalcart.stock.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantitySearchCondition {

    private Long productId;
    private List<Long> optionDetailsIds;

    public static StockQuantitySearchCondition of(Long productId, List<Long> optionDetailsIds) {
        return new StockQuantitySearchCondition(productId, optionDetailsIds);
    }

}
