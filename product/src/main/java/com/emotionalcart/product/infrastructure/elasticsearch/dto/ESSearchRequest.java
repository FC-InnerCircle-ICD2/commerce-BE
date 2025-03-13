package com.emotionalcart.product.infrastructure.elasticsearch.dto;

import com.emotionalcart.product.domain.dto.ProductSearch;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ESSearchRequest {
    private String keyword;
    private Long categoryId;
    private int page;
    private int size;
    private String sortOption; //[ PRICE_ASC, PRICE_DESC, SALES_ASC, SALES_DESC, CREATE_ASC, CREATE_DESC ]
    private Integer minPrice;
    private Integer maxPrice;

    public static ESSearchRequest convert(ProductSearch productSearch){
        return new ESSearchRequest(
            productSearch.getKeyword(),
            productSearch.getCategoryId() != null ? productSearch.getCategoryId() : null,
            productSearch.getPageRequest() != null ? productSearch.getPageRequest().getPageNumber()+1 : 1,
            productSearch.getPageRequest() != null ? productSearch.getPageRequest().getPageSize() : 10,
            productSearch.getSortOption() != null ? productSearch.getSortOption().name() : "CREATE_DESC",
            productSearch.getPriceMin() != null ? productSearch.getPriceMin().intValue() : null,
            productSearch.getPriceMax() != null ? productSearch.getPriceMax().intValue() : null
        );
    }
}
