package com.core.base;

import org.springframework.data.domain.PageRequest;

public abstract class BasePageRequest {
    int pageSize = 10;
    int pageNumber = 0;
    // TODO - sort

    public PageRequest getPageable() {
        return PageRequest.of(this.pageNumber, this.pageSize);
    }
}
