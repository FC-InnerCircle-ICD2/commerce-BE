package com.emotionalcart.order.infra.stock.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockQuantityOptionValidateRequest {

    private List<Long> optionDetailsIds;

    private Integer quantity;

    public StockQuantityOptionValidateRequest(List<Long> optionDetailsIds, int quantity) {
        this.optionDetailsIds = optionDetailsIds;
        this.quantity = quantity;
    }

    public static StockQuantityOptionValidateRequest of(int quantity, List<Long> optionDetailsIds) {
        return new StockQuantityOptionValidateRequest(optionDetailsIds, quantity);
    }

}