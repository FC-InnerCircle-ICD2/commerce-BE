package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.application.dto.GetProductReviews;
import com.emotionalcart.product.domain.ProductDataProvider;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDataProvider productDataProvider;

    public Page<GetProductReviews.Response> getProductReviews(@NotNull Long productId, GetProductReviews.Request request) {
        productDataProvider.findProduct(productId);

        Page<Review> reviews = productDataProvider.findAllReviews(productId, request.getPageable());
        ReviewImages reviewImages = findAllReviewImages(reviews.getContent());

        return GetProductReviews.Response.toResponse(reviews, reviewImages);
    }

    private ReviewImages findAllReviewImages(List<Review> reviews) {
        Reviews from = Reviews.from(reviews);
        return ReviewImages.from(productDataProvider.findAllReviewImages(from.ids()));
    }
}
