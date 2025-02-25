package com.emotionalcart.adminproduct.domain;

import com.emotionalcart.adminproduct.infrastructure.AdminProducts;
import com.emotionalcart.adminproduct.infrastructure.repository.AdminProductRepository;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProductDataProvider {
    private final AdminProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product findProductById(Long productId){
        return productRepository.findByIdAndIsDeletedIsFalse(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    public Page<AdminProducts> findAllProduct(PageRequest pageRequest) {
        return productRepository.findAllProducts(pageRequest);
    }
}
