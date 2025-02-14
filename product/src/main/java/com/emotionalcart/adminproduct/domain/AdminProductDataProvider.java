package com.emotionalcart.adminproduct.domain;

import com.emotionalcart.adminproduct.infrastructure.repository.AdminProductRepository;
import com.emotionalcart.core.feature.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductDataProvider {
    private final AdminProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
}
