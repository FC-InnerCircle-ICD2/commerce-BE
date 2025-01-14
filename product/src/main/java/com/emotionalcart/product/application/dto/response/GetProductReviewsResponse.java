package com.emotionalcart.product.application.dto.response;

import com.emotionalcart.product.domain.Review;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetProductReviewsResponse {
    private Long id;
    private String productName;
    private String productOptionName;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private List<ReviewImageResponse> reviewImages;

    public GetProductReviewsResponse(Review review, List<ReviewImageResponse> reviewImages) {
        this.id = review.getId();
        this.productName = review.getProductName();
        this.productOptionName = review.getProductOptionName();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.reviewImages = reviewImages;
    }
}