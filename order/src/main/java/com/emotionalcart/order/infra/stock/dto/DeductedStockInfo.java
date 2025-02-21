package com.emotionalcart.order.infra.stock.dto;

import java.util.List;

public record DeductedStockInfo(Long stockId, List<Long> optionDetailIds, int stockQuantity) {

    public static DeductedStockInfo of(Long stockId, List<Long> optionDetailIds, int stockQuantity) {
        return new DeductedStockInfo(stockId, optionDetailIds, stockQuantity);
    }

}

