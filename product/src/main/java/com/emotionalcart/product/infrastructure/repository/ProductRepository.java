package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p.category.id FROM Product p WHERE p.id = :productId AND p.isDeleted = false")
    Optional<Long> findCategoryIdByIdAndIsDeletedIsFalse(@Param("productId") Long productId);

    Optional<Product> findByIdAndIsDeletedIsFalse(@NotNull Long productId);

    @Query("SELECT p.provider.id FROM Product p WHERE p.id = :productId AND p.isDeleted = false")
    Optional<Long> findProviderIdByIdAndIsDeletedIsFalse(@Param("productId") Long productId);
}
