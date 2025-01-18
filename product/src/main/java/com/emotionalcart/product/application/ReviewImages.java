package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.review.ReviewImage;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReviewImages {

    private final List<ReviewImage> reviewImages;

    private ReviewImages(List<ReviewImage> reviewImages) {
        this.reviewImages = reviewImages;
    }

    public static ReviewImages from(List<ReviewImage> reviewImages) {
        return new ReviewImages(reviewImages);
    }


    public Map<Long, List<ReadProductReviews.ReviewImageResponse>> groupByReviewId() {
        return reviewImages.stream()
                .collect(Collectors.groupingBy(
                        ReviewImage::getReviewId,
                        Collectors.mapping(ReadProductReviews.ReviewImageResponse::new, Collectors.toList())
                ));
    }

}
