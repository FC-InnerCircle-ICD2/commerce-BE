package com.emotionalcart.product.presentation.dto.request;

import com.emotionalcart.core.feature.review.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateProductReviewRequest {
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
}
