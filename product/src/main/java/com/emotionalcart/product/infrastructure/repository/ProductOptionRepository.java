package com.emotionalcart.product.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emotionalcart.core.feature.product.ProductOption;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    Optional<List<ProductOption>> findAllByProductIdAndIsDeletedIsFalseAndIsRequiredIsTrue(Long productId);
}
