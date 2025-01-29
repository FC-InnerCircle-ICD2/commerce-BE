package com.emotionalcart.product.domain.support;

import com.emotionalcart.product.domain.dto.ProductDetail;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDetails {
    private final Map<Long, List<ProductDetail>> groupedDetails;

    private ProductDetails(Map<Long, List<ProductDetail>> groupedDetails) {
        this.groupedDetails = groupedDetails;
    }

    public static ProductDetails from(List<ProductDetail> productDetails) {
        Map<Long, List<ProductDetail>> groupedDetails = productDetails.stream()
                .collect(Collectors.groupingBy(ProductDetail::getProductId));
        return new ProductDetails(groupedDetails);
    }

    public List<ProductDetail> getDetailsByProductId(Long productId) {
        return groupedDetails.getOrDefault(productId, Collections.emptyList());
    }

    public List<ProductDetail> filterByOptionDetailIds(Set<Long> optionDetailIds) {
        return groupedDetails.values().stream()
                .flatMap(List::stream)
                .filter(detail -> optionDetailIds.contains(detail.getProductOptionDetailId()))
                .collect(Collectors.toList());
    }
}