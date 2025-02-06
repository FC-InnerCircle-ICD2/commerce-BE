package com.emotionalcart.order.presentation.controller.response;

import com.emotionalcart.order.infra.dto.BestSellingProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SalesRankingResponse {

    private Page<BestSellingProduct> productList;

    public SalesRankingResponse(Page<BestSellingProduct> productList) {
        this.productList = productList;
    }

    public static SalesRankingResponse from(Page<BestSellingProduct> productRankingsByCategoryId) {
        return new SalesRankingResponse(productRankingsByCategoryId);
    }

}
