package com.emotionalcart.product.infrastructure.stock.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockQuantitySearchRequest {

    private Long productId;

    private List<Long> optionDetailsIds;

    public StockQuantitySearchRequest(Long productId, List<Long> optionDetailsIds) {
        this.productId = productId;
        this.optionDetailsIds = optionDetailsIds;
    }
}
