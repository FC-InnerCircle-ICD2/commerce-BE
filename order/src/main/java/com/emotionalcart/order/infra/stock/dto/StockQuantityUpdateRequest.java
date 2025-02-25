package com.emotionalcart.order.infra.stock.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockQuantityUpdateRequest {

    private Long productId;

    private List<Long> optionDetailIds;

    private Integer quantity;

    public StockQuantityUpdateRequest(Long productId, List<Long> optionDetailIds, int quantity) {
        this.productId = productId;
        this.optionDetailIds = optionDetailIds;
        this.quantity = quantity;
    }

    public static StockQuantityUpdateRequest of(Long productId, List<Long> optionDetailIds, int quantity) {
        return new StockQuantityUpdateRequest(productId, optionDetailIds, quantity);
    }

}
