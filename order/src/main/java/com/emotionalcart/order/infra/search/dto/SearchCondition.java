package com.emotionalcart.order.infra.search.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCondition {

    protected String keyword;

    protected int page = 1;

    protected int size = 10;

    protected SortOption sortOption = SortOption.CREATE_DESC;

}
