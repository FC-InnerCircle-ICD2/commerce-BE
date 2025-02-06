package com.emotionalcart.product.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emotionalcart.core.feature.product.ProductOption;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    Optional<List<ProductOption>> findAllByProduct_IdAndIsDeletedIsFalse(Long productId); // 시연 위해 AndIsRequiredIsTrue 제거. 나중에 실제 데이터에서는 추가
}