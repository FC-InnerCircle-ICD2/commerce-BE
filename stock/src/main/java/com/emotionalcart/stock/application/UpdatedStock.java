package com.emotionalcart.stock.application;

import java.util.List;

public record UpdatedStock(Long stockId, List<Long> optionDetailId, int stockQuantity) {

    public static UpdatedStock of(Long stockId, List<Long> optionDetailId, int stockQuantity) {
        return new UpdatedStock(stockId, optionDetailId, stockQuantity);
    }

}
