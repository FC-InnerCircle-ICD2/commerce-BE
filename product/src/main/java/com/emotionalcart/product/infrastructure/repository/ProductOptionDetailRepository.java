package com.emotionalcart.product.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emotionalcart.core.feature.product.ProductOptionDetail;

@Repository
public interface ProductOptionDetailRepository extends JpaRepository<ProductOptionDetail, Long> {
    Optional<List<ProductOptionDetail>> findAllByProductOptionIdAndIsDeletedIsFalse(Long productOptionId);
}
