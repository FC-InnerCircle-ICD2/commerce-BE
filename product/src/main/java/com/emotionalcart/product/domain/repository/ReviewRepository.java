package com.emotionalcart.product.domain.repository;

import com.emotionalcart.product.domain.Review;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByProductIdAndIsDeletedIsFalse(@NotNull Long productId, PageRequest pageRequest);
}
