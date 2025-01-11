package com.emotionalcart.product.domain.repository;

import com.emotionalcart.product.domain.entity.ProductCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCategoryRespository {
    Page<ProductCategory> findAll(Pageable pageable);
//    ProductCategory save(ProductCategory productCategory);
}
