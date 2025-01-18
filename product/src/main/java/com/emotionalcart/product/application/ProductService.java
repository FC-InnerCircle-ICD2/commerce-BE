package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.application.dto.GetProductReviews;
import com.emotionalcart.product.application.dto.ReadCategories;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.core.feature.category.Category;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDataProvider productDataProvider;

    public List<ReadCategories.Response> getAllProductCategories() {
        List<Category> categories = productDataProvider.findAllCategories();

        return ReadCategories.Response.fromCategories(categories);
    }

    public Page<ReadProductReviews.Response> readProductReviews(@NotNull Long productId, ReadProductReviews.Request request) {
        productDataProvider.findProduct(productId);

        Page<Review> reviews = productDataProvider.findAllReviews(productId, request.getPageable());
        ReviewImages reviewImages = findAllReviewImages(reviews.getContent());

        return ReadProductReviews.Response.toResponse(reviews, reviewImages);
    }

    private ReviewImages findAllReviewImages(List<Review> reviews) {
        Reviews from = Reviews.from(reviews);
        return ReviewImages.from(productDataProvider.findAllReviewImages(from.ids()));
    }
}
