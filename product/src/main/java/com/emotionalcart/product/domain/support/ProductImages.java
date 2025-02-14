package com.emotionalcart.product.domain.support;

import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.product.presentation.dto.ReadProductImages;
import com.emotionalcart.product.presentation.dto.ReadProducts;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductImages {
    private final List<ProductImage> productImages;

    private ProductImages(List<ProductImage> productImages) {this.productImages = productImages;}

    public static ProductImages from(List<ProductImage> productImages) {
        return new ProductImages(productImages);
    }

    public Map<Product, List<ReadProductImages.Response>> groupByProductId() {
        return productImages.stream()
                .collect(Collectors.groupingBy(
                        ProductImage::getProduct,
                        Collectors.mapping(ReadProductImages.Response::new, Collectors.toList())
                ));
    }
}
