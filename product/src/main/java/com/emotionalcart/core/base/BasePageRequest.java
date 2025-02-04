package com.emotionalcart.core.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
public abstract class BasePageRequest {
    private Integer pageSize = 10;
    private Integer pageNumber = 0;

    public PageRequest getPageable() {
        int size = (pageSize != null && pageSize > 0 && pageSize <= 100) ? pageSize : 10;
        int page = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;

        return PageRequest.of(page, size);
    }
}
