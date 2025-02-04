package com.emotionalcart.product.infrastructure.repository;

import java.util.List;

import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewStatistic;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewStatisticRepository extends JpaRepository<ReviewStatistic, Long> {
    List<ReviewStatistic> findAllByProductIdIn(List<Long> productId);
  
    Optional<ReviewStatistic> findByProductId(@NotNull Long productId);
}
