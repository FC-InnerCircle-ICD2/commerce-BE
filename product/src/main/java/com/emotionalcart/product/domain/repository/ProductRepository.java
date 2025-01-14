package com.emotionalcart.product.domain.repository;

import com.emotionalcart.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByIdAndIsDeletedIsFalse(@NotNull Long productId);
}
