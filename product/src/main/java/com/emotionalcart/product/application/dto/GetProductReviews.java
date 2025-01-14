package com.emotionalcart.product.application.dto;

import com.core.base.BasePageRequest;
import com.core.feature.review.Review;
import com.core.feature.review.ReviewImage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class GetProductReviews {

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

        public static Page<Response> toResponse(Page<Review> reviews, Map<Long, List<ReviewImageResponse>> reviewImages) {
            return reviews.map(review -> {
                List<ReviewImageResponse> images = reviewImages.getOrDefault(review.getId(), List.of());
                return new Response(review, images);
            });
        }
    }

    @Data
    public class ReviewImageResponse {
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
