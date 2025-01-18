package com.emotionalcart.product.infrastructure.repository;


import com.emotionalcart.product.domain.dto.ProductDetail;

import java.util.List;
import java.util.Set;


public interface QueryDslProductRepository {
    List<ProductDetail> findAllProductData(Set<Long> productIds);
}
