package com.emotionalcart.order.presentation.controller.response;

import com.emotionalcart.order.domain.dto.BestSellingProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SalesRankingResponse {

    private List<BestSellingProduct> productList;

    public SalesRankingResponse(List<BestSellingProduct> productList) {
        this.productList = productList;
    }

    public static SalesRankingResponse from(List<BestSellingProduct> productRankingsByCategoryId) {
        return new SalesRankingResponse(productRankingsByCategoryId);
    }

}
