package com.emotionalcart.product.domain.support;

import com.emotionalcart.core.feature.product.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Products {
    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products from(Page<Product> productPage) {
        return new Products(productPage.getContent());
    }

    public static Products from(List<Product> products) {
        return new Products(products);
    }

    public List<Long> ids(){
        return this.products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }

    public List<Long> getCategoryIds() {
        return this.products.stream()
                .map(Product::getCategoryId)
                .collect(Collectors.toList());
    }

    public List<Long> getProviderIds() {
        return this.products.stream()
                .map(Product::getProviderId)
                .collect(Collectors.toList());
    }
}
