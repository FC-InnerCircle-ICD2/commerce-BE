package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.review.Review;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByProductIdAndIsDeletedIsFalse(@NotNull Long productId, PageRequest pageRequest);
    Optional<Review> findByProductIdAndUserIdAndIsDeletedIsFalse(Long productId, Long userId);
}
