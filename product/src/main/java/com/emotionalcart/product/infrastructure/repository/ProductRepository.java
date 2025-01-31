package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QueryDslProductRepository {
    Optional<Product> findByIdAndIsDeletedIsFalse(@NotNull Long productId);
}
