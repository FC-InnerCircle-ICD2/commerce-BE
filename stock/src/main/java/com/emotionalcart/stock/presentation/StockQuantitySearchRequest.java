package com.emotionalcart.stock.presentation;

import com.emotionalcart.stock.application.StockQuantitySearchQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockQuantitySearchRequest {

    private Long productId;

    private List<Long> optionDetailsIds;

    public StockQuantitySearchQuery mapToQuery() {
        return StockQuantitySearchQuery.builder().productId(productId).optionDetailsIds(optionDetailsIds).build();
    }

}
