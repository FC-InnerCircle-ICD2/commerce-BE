package com.emotionalcart.core.base;

import com.emotionalcart.product.presentation.dto.ReadProducts;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public abstract class BasePageRequest {
    int pageSize = 10;
    int pageNumber = 0;
    // 기본 정렬: 신상품순 (createdAt 내림차순)
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    public PageRequest getPageable() {
        return PageRequest.of(this.pageNumber, this.pageSize, this.sort);
    }

    public void setSort(ReadProducts.SortOption sortOption) {
        Sort.Direction sortDirection = Sort.Direction.fromString(sortOption.getDirection());
        this.sort = Sort.by(sortDirection, sortOption.getField());
    }
}
