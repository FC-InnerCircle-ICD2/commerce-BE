package com.emotionalcart.product.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import com.emotionalcart.core.feature.product.ProductImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emotionalcart.core.feature.product.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<List<ProductImage>> findAllByProduct_IdInAndIsDeletedIsFalseAndImageType(List<Long> productIds, ProductImageType imageType);

    Optional<List<ProductImage>> findAllByProduct_IdAndIsDeletedIsFalse(Long productId);
}
