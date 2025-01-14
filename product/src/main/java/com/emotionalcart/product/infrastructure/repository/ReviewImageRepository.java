package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.review.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findAllByReviewIdInAndIsDeletedIsFalse(List<Long> reviewIds);
}
