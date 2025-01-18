package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.application.dto.ReadCategories;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
import com.emotionalcart.product.presentation.dto.ReadProductStock;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    public List<ReadProductStock.Response> readProductStock(List<ReadProductStock.Request> requests) {
        return requests.stream()
                .map(productDataProvider::findProductStock)
                .toList();
    }
}
