package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.base.BasePageRequest;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewImage;
import com.emotionalcart.product.application.ReviewImages;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReadProductReviews {

    @Getter
    @Setter
    public static class Request extends BasePageRequest {
        int pageSize = 10;
        int pageNumber = 0;
    }

    @Data
    public static class Response {
        private Long id;
        private String productName;
        private String productOptionName;
        private Integer rating;
        private String content;
        private LocalDateTime createdAt;
        private List<ReviewImageResponse> reviewImages;

        public Response(Review review, List<ReviewImageResponse> reviewImages) {
            this.id = review.getId();
            this.productName = review.getProductName();
            this.productOptionName = review.getProductOptionName();
            this.rating = review.getRating();
            this.content = review.getContent();
            this.createdAt = review.getCreatedAt();
            this.reviewImages = reviewImages;
        }

        public static Page<Response> toResponse(Page<Review> reviews, ReviewImages reviewImages) {
            Map<Long, List<ReviewImageResponse>> reviewImagesMap = reviewImages.groupByReviewId();

            return reviews.map(review -> {
                List<ReviewImageResponse> images = reviewImagesMap.getOrDefault(review.getId(), List.of());
                return new Response(review, images);
            });
        }
    }

    @Data
    public static class ReviewImageResponse {
        private Long id;
        private String url;
        private Integer fileOrder;

        public ReviewImageResponse(ReviewImage reviewImage) {
            this.id = reviewImage.getId();
            this.url = reviewImage.getFilePath();
            this.fileOrder = reviewImage.getFileOrder();
        }
    }
}
