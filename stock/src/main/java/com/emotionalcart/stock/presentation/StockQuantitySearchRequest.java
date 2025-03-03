package com.emotionalcart.stock.presentation;

import com.emotionalcart.stock.application.StockQuantitySearchQuery;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockQuantitySearchRequest {

    @NotNull(message = "상품 식별자는 필수입니다.")
    private Long productId;

    @NotEmpty(message = "옵션 디테일 식별자 목록은 필수입니다.")
    private List<Long> optionDetailsIds;

    public StockQuantitySearchQuery mapToQuery() {
        return StockQuantitySearchQuery.builder().productId(productId).optionDetailsIds(optionDetailsIds).build();
    }

}
