package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewImage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreateProductReview {
    @Getter
    @Setter
    public static class Request {
        private String productName;
        private String productOptionId;
        private String productOptionName;
        @NotNull
        @Min(1)
        @Max(5)
        private Integer rating;
        private String content;
        private List<MultipartFile> reviewImages;

        public Review toReviewEntity(Long productId) {
            return Review.of(
                    productId,
                    productName,
                    productOptionId,
                    productOptionName,
                    rating,
                    content
            );
        }

        public List<ReviewImage> toReviewImageEntities(Long reviewId) {
            return Optional.ofNullable(reviewImages)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(file -> ReviewImage.of(
                            reviewId,
                            file.getOriginalFilename(),
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getSize(),
                            reviewImages.indexOf(file) + 1
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        Long reviewId;
    }
}
