package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.review.ReviewStatistic;

import lombok.Data;

public class ReadProductReviewStatistic {

    @Data
    public static class Response {
        private Double averageRating;
        private Integer reviewCount;

        public Response(Double averageRating, Integer reviewCount) {
            this.averageRating = averageRating;
            this.reviewCount = reviewCount;
        }

        public static Response toResponse(ReviewStatistic reviewStatistic) {
            return new Response(reviewStatistic.getAverageRating(), reviewStatistic.getReviewCount());
        }
    }

}
