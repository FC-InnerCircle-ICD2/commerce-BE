package com.emotionalcart.product.domain;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.provider.Provider;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewImage;
import com.emotionalcart.product.domain.dto.ProductDetail;
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
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductDataProvider {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionDetailRepository productOptionDetailRepository;

    // 상품 관련 메서드
    public Product findProduct(Long productId) {
        return productRepository.findByIdAndIsDeletedIsFalse(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT));
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

    public List<ProductDetail> findAllProductDetail(Set<Long> productIds) {
        return productRepository.findAllProductDetail(productIds);
    }
}
