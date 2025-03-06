package com.emotionalcart.order.infra.search.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortOption {

    PRICE_ASC("price", "ASC"),
    PRICE_DESC("price", "DESC"),
    SALES_ASC("salesCount", "ASC"),
    SALES_DESC("salesCount", "DESC"),
    CREATE_ASC("createdAt", "ASC"),
    CREATE_DESC("createdAt", "DESC"),
    ;
    private final String field;
    private final String direction;

}
