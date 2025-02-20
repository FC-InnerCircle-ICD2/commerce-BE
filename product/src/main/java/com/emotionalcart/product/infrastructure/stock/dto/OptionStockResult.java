package com.emotionalcart.product.infrastructure.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OptionStockResult {
    private List<OptionStocksResponse> optionStocksResponses;
    private int totalStockQuantity;
}
