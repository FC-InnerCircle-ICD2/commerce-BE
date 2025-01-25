package com.emotionalcart.core.feature.product;

import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@Getter
public enum SortOption {
    PRICE_ASC("price", "ASC"),
    PRICE_DESC("price", "DESC"),
    SALES_DESC("sales", "DESC"),
    CREATE_DESC("createdAt","DESC");

    private final String field;
    private final String direction;

    SortOption(String field, String direction) {
        this.field = field;
        this.direction = direction;
    }

    public static SortOption from(Sort.Order order) {
        return Arrays.stream(values())
                .filter(option -> option.field.equals(order.getProperty())
                        && option.direction.equals(order.getDirection().name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown sorting property: " + order.getProperty()));
    }
}