package com.emotionalcart.product.infrastructure.stock.dto;

import com.emotionalcart.product.domain.support.OptionDetailsGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionStocksResponse {
    private List<OptionStockDto> options;
    private Integer stockQuantity; // 재고 수량

    public OptionStocksResponse(List<OptionStockDto> options, Integer stockQuantity) {
        this.options = options;
        this.stockQuantity = stockQuantity;
    }

    public static OptionStocksResponse toResponse(OptionDetailsGroup optionDetailsGroup, Integer stockQuantity) {
        return new OptionStocksResponse(optionDetailsGroup.getOptions(), stockQuantity);
    }
}
