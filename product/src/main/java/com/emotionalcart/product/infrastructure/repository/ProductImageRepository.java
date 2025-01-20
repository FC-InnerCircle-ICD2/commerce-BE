package com.emotionalcart.product.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emotionalcart.core.feature.product.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<List<ProductImage>> findAllByProductOptionDetailIdAndIsDeletedIsFalseOrderByIsRepresentativeAscFileOrderAsc(
            Long productOptionDetailId);
}
