package com.emotionalcart.product.domain.support;

import com.emotionalcart.core.feature.product.Product;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Products {
    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }
    public static Products from(List<Product> products) {
        return new Products(products);
    }

    public Set<Long> ids(){
        return this.products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }
}
