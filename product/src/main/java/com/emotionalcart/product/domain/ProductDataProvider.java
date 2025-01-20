package com.emotionalcart.product.domain;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.product.Provider;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewImage;
import com.emotionalcart.product.infrastructure.repository.CategoryRepository;
import com.emotionalcart.product.infrastructure.repository.ProductImageRepository;
import com.emotionalcart.product.infrastructure.repository.ProductOptionDetailRepository;
import com.emotionalcart.product.infrastructure.repository.ProductOptionRepository;
import com.emotionalcart.product.infrastructure.repository.ProductRepository;
import com.emotionalcart.product.infrastructure.repository.ProviderRepository;
import com.emotionalcart.product.infrastructure.repository.ReviewImageRepository;
import com.emotionalcart.product.infrastructure.repository.ReviewRepository;
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
    private final ProductImageRepository productImageRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionDetailRepository productOptionDetailRepository;
    private final ProviderRepository providerRepository;

    // 상품 관련 메서드
    public Product findProduct(Long productId) {
        return productRepository.findByIdAndIsDeletedIsFalse(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    public Long findCategoryIdByProductId(Long productId) {
        return productRepository.findCategoryIdByIdAndIsDeletedIsFalse(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    public Long findProviderIdByProductId(Long productId) {
        return productRepository.findProviderIdByIdAndIsDeletedIsFalse(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PROVIDER));
    }

    // 카테고리 관련 메서드
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findByIdAndIsDeletedIsFalse(categoryId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_CATEGORY));
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAllByIsActiveIsTrueAndIsDeletedIsFalse();
    }

    // 리뷰 관련 메서드
    public Page<Review> findAllReviews(Long productId, PageRequest pageRequest) {
        return reviewRepository.findAllByProductIdAndIsDeletedIsFalse(productId, pageRequest);
    }

    public List<ReviewImage> findAllReviewImages(List<Long> reviewIds) {
        return reviewImageRepository.findAllByReviewIdInAndIsDeletedIsFalse(reviewIds);
    }

    // 상품 이미지 관련 메서드
    public List<ProductImage> findAllProductImagesByProductOptionDetailId(Long productOptionDetailId) {
        return productImageRepository
                .findAllByProductOptionDetailIdAndIsDeletedIsFalseOrderByIsRepresentativeAscFileOrderAsc(
                        productOptionDetailId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_IMAGE));
    }

    // 상품 옵션 관련 메서드
    public List<ProductOption> findAllProductOptionsByProductId(Long productId) {
        return productOptionRepository.findAllByProductIdAndIsDeletedIsFalseAndIsRequiredIsTrue(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION));
    }

    public List<ProductOptionDetail> findAllProductOptionDetailsByProductOptionId(Long productOptionId) {
        return productOptionDetailRepository.findAllByProductOptionIdAndIsDeletedIsFalse(productOptionId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION_DETAIL));
    }

    // 공급자 관련 메서드
    public Provider findProviderById(Long providerId) {
        return providerRepository.findByIdAndIsDeletedIsFalse(providerId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PROVIDER));
    }
}
