package com.emotionalcart.product.application;

import com.core.feature.review.Review;
import com.core.feature.review.ReviewImage;
import com.emotionalcart.product.application.dto.GetProductReviews;
import com.emotionalcart.product.domain.ProductDataProvider;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDataProvider productDataProvider;

    public Page<GetProductReviews.Response> getProductReviews(@NotNull Long productId, GetProductReviews.Request request) {
        productDataProvider.findProduct(productId);

        Page<Review> reviews = productDataProvider.findAllReviews(productId, request.getPageable());
//        List<GetProductReviews.ReviewImageResponse> reviewImages = fetchReviewImages(reviews);

        return GetProductReviews.Response.toResponse(reviews, Map.of());
    }
}
