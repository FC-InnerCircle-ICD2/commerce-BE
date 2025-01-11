package com.emotionalcart.product.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Pagination {
    private int page;
    private int size;
    private Long totalElements;
    private Integer totalPage;
    private Integer currentElements;
    private Integer currentPage;
    private String orderBy;
    private String sortBy;
    private Long limit;

    public Pagination complete(Page value, Pageable pageable) {
        return complete(value, pageable, value.getTotalElements());
    }

    public Pagination complete(Slice value, Pageable pageable, Long cnt) {
        this.page = pageable.getPageNumber() + 1;
        this.size = value.getSize();
        this.totalElements = cnt;
        this.totalPage = (int) Math.ceil((double) totalElements / size);
        this.currentElements = value.getNumberOfElements();
        this.currentPage = value.getPageable().getPageNumber() + 1;
        this.limit = value.getPageable().getOffset();
        String[] sort = pageable.getSort().toString().split(":");
        this.orderBy = sort[0];
        this.sortBy = sort[1].trim();
        return this;
    }
}
