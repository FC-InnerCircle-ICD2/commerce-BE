package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.product.Product;

import java.util.List;

public class Products {
    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }
    public static Products from(List<Product> products) {
        return new Products(products);
    }

    public List<Long> ids(){
        return this.products.stream()
                .map(Product::getId)
                .toList();
    }
}
