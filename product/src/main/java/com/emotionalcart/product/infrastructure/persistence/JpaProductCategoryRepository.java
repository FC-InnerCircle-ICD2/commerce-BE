package com.emotionalcart.product.infrastructure.persistence;

import com.emotionalcart.product.domain.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductCategoryRepository extends JpaRepository<ProductCategory,Long> {
//    Page<ProductCategory> findAll(Pageable pageable);
//    ProductCategory save(ProductCategory productCategory);
}
