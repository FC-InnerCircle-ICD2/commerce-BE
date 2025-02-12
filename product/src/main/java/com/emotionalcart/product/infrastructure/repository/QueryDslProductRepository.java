package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.*;
import com.emotionalcart.product.domain.dto.ProductSearch;
import org.springframework.data.domain.Page;
import com.emotionalcart.product.domain.dto.ProductDetail;

import java.util.List;
import java.util.Set;

public interface QueryDslProductRepository {
    Page<Product> findAllProducts(ProductSearch productSearch);

    List<ProductOption> findProductOptions(List<Long> productIds);

    List<ProductDetail> findAllProductDetail(Set<Long> productIds);
}
