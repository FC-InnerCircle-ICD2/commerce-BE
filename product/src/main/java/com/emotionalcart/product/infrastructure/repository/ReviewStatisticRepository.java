package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.review.ReviewStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewStatisticRepository  extends JpaRepository<ReviewStatistic, Long> {
    List<ReviewStatistic> findAllByProductIdIn(List<Long> productId);
}
