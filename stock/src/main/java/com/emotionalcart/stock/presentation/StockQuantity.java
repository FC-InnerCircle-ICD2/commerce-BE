package com.emotionalcart.stock.presentation;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StockQuantity {

    private Long productId;

    private List<Long> optionDetailsIds;

    private Integer quantity;

}
