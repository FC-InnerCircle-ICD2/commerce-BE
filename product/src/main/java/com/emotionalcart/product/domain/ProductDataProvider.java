package com.emotionalcart.product.domain;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewImage;
import com.emotionalcart.core.feature.review.ReviewStatistic;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.infrastructure.repository.*;
import com.emotionalcart.product.presentation.dto.ReadProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductDataProvider {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewStatisticRepository reviewStatisticRepository;

    public Product findProduct(Long productId) {
        return productRepository.findByIdAndIsDeletedIsFalse(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    public Page<Review> findAllReviews(Long productId, PageRequest pageRequest) {
        return reviewRepository.findAllByProductIdAndIsDeletedIsFalse(productId, pageRequest);
    }

    public List<ReviewImage> findAllReviewImages(List<Long> reviewIds) {
        return reviewImageRepository.findAllByReviewIdInAndIsDeletedIsFalse(reviewIds);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAllByIsActiveIsTrueAndIsDeletedIsFalse();
    }

    public Page<Product> findAllProducts(ReadProducts.Request request, PageRequest pageRequest) {
        return productRepository.findAllProducts(request, pageRequest);
    }

    public List<ProductOption> findProductOptions(Set<Long> productIds) {
        return productRepository.findProductOptions(productIds);
    }

    public List<ProductOptionDetailWithImages> findProductOptionDetails(Set<Long> optionIds) {
        return productRepository.findProductOptionDetailsWithImages(optionIds);
    }

    public Map<Long, Double> findProductRatings(List<Long> productIds) {
        List<ReviewStatistic> ratings = reviewStatisticRepository.findAllByProductIdIn(productIds);

        // Map으로 변환
        return ratings.stream()
                .collect(Collectors.toMap(
                        ReviewStatistic::getProductId,
                        ReviewStatistic::getAverageRating
                ));
    }
}
