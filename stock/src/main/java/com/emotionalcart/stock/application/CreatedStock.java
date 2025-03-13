package com.emotionalcart.stock.application;

import java.util.List;

public record CreatedStock(Long productId, Long stockId, List<Long> optionDetailsId) {

    public static CreatedStock of(Long productId, Long stockId, List<Long> optionDetailsId) {
        return new CreatedStock(productId, stockId, optionDetailsId);
    }

}
