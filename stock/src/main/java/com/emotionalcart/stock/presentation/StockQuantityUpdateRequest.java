package com.emotionalcart.stock.presentation;

import lombok.Getter;

import java.util.List;

@Getter
public class StockQuantityUpdateRequest {

    private Long productId;

    private List<Long> optionDetailIds;

    private Integer quantity;

}
