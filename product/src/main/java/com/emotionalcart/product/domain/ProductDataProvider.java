package com.emotionalcart.product.domain;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewImage;
import com.emotionalcart.core.feature.review.ReviewStatistic;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.domain.dto.ProductSearch;
import com.emotionalcart.product.infrastructure.repository.*;
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
    private final ProductImageRepository productImageRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionDetailRepository productOptionDetailRepository;
    private final ReviewStatisticRepository reviewStatisticRepository;

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

    public void findProductReview(Long productId, String userId) {
        reviewRepository.findByProductIdAndUserIdAndIsDeletedIsFalse(productId, userId)
                .ifPresent(review -> {
                    throw new ProductException(ErrorCode.DUPLICATE_REVIEW);
                });
    }

    public void saveProductReview(Review review) {
        reviewRepository.save(review);
    }

    public void saveProductReviewImages(List<ReviewImage> reviewImages) {
        reviewImageRepository.saveAll(reviewImages);
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
        return productOptionRepository.findAllByProduct_IdAndIsDeletedIsFalse(productId) // 시연 위해 AndIsRequiredIsTrue 제거. 나중에 실제 데이터에서는 추가
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION));
    }

    public List<ProductOptionDetail> findAllProductOptionDetailsByProductOptionId(Long productOptionId) {
        return productOptionDetailRepository.findAllByProductOptionIdAndIsDeletedIsFalse(productOptionId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION_DETAIL));
    }

    public List<ProductDetail> findAllProductDetail(Set<Long> productIds) {
        return productRepository.findAllProductDetail(productIds);
    }

    public ReviewStatistic findReviewStatistic(Long productId) {
        return reviewStatisticRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_REVIEW_STATISTIC));
    }

    public Page<Product> findAllProducts(ProductSearch productSearch) {
        return productRepository.findAllProducts(productSearch);
    }

    public List<ProductOption> findProductOptions(List<Long> productIds) {
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
