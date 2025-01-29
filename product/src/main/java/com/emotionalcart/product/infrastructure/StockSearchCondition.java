package com.emotionalcart.product.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class StockSearchCondition {
    private Long productId;
    private Map<Long, Long> optionMap;
}