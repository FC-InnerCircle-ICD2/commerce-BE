package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.product.presentation.dto.ReadProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface QueryDslProductRepository {
    Page<Product> findAllProducts(ReadProducts.Request request, PageRequest pageRequest);

    List<ProductOption> findProductOptions(List<Long> productIds);
}
