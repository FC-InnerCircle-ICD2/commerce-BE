package com.emotionalcart.product.domain.support;

import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.presentation.dto.ReadProducts;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductOptionDetails {

    private final List<ProductOptionDetailWithImages> optionDetails;

    private ProductOptionDetails(List<ProductOptionDetailWithImages> optionDetails) {
        this.optionDetails = optionDetails;
    }

    public static ProductOptionDetails from(List<ProductOptionDetailWithImages> options){
        return new ProductOptionDetails(options);
    }

    // Option ID 기준으로 그룹화
    public Map<Long, List<ReadProducts.ProductOptionDetailResponse>> groupByOptionId() {
        return optionDetails.stream().collect(Collectors.groupingBy(
                ProductOptionDetailWithImages::getOptionId,
                Collectors.mapping(
                        ReadProducts.ProductOptionDetailResponse::new,
                        Collectors.toList()
                )
        ));
    }
}
