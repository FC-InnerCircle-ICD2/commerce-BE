package com.emotionalcart.stock.application;

import com.emotionalcart.stock.domain.StockQuantitySearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantitySearchQuery {

    private Long productId;
    private List<Long> optionDetailsIds;

    public StockQuantitySearchCondition mapToCondition() {
        return StockQuantitySearchCondition.builder()
            .productId(productId)
            .optionDetailsIds(optionDetailsIds)
            .build();
    }

}
