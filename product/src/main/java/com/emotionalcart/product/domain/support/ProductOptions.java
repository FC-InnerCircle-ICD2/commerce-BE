package com.emotionalcart.product.domain.support;

import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.product.presentation.dto.ReadProducts;

import java.util.*;
import java.util.stream.Collectors;

public class ProductOptions {

    private final List<ProductOption> options;

    private ProductOptions(List<ProductOption> options) {
        this.options = options;
    }

    public static ProductOptions from(List<ProductOption> options) {
        return new ProductOptions(options);
    }

    // Product ID 기준으로 그룹화
    public Map<Long, List<ReadProducts.ProductOptionResponse>> groupByProductId() {
        return options.stream()
                .collect(Collectors.groupingBy(
                        ProductOption::getProduct,
                        Collectors.mapping(ReadProducts.ProductOptionResponse::new, Collectors.toList())
                ));
    }

    public Set<Long> ids(){
        return this.options.stream()
                .map(ProductOption::getId)
                .collect(Collectors.toSet());
    }
}
