package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.adminproduct.infrastructure.AdminProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AdminQueryDslProductRepository {
    Page<AdminProducts> findAllProducts(PageRequest pageRequest);
}
