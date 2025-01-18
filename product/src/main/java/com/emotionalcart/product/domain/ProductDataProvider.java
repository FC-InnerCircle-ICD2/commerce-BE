package com.emotionalcart.product.domain;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewImage;
import com.emotionalcart.product.infrastructure.repository.CategoryRepository;
import com.emotionalcart.product.infrastructure.repository.ProductRepository;
import com.emotionalcart.product.infrastructure.repository.ReviewImageRepository;
import com.emotionalcart.product.infrastructure.repository.ReviewRepository;
import com.emotionalcart.product.presentation.dto.ReadProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDataProvider {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final CategoryRepository categoryRepository;

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

    public ReadProductStock.Response findProductStock(ReadProductStock.Request request) {
        return productRepository.findProductsStock(request)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION));
    }
}
