package com.emotionalcart.product.application;

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

    public Set<Long> getAllOptionIds(Long productId) {
        List<ProductDetail> productDetails = groupedDetails.get(productId);
        if (productDetails == null) {
            return Collections.emptySet();
        }
        return productDetails.stream()
                .map(ProductDetail::getProductOptionId)
                .collect(Collectors.toSet());
    }
    public Set<Long> getAllOptionDetailIds(Long productId) {
        return groupedDetails.getOrDefault(productId, Collections.emptyList()).stream()
                .map(ProductDetail::getProductOptionDetailId)
                .collect(Collectors.toSet());
    }

    public Set<Long> getRequiredOptionIds(Long productId) {
        List<ProductDetail> productDetails = groupedDetails.get(productId);
        if (productDetails == null) {
            return Collections.emptySet();
        }
        return productDetails.stream()
                .filter(ProductDetail::isRequired)
                .map(ProductDetail::getProductOptionId)
                .collect(Collectors.toSet());
    }
}