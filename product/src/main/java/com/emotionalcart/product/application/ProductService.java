package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.presentation.dto.ReadCategories;
import com.emotionalcart.product.presentation.dto.ReadProductCategories;
import com.emotionalcart.product.presentation.dto.ReadProductDetails;
import com.emotionalcart.product.presentation.dto.ReadProductOptionDetails;
import com.emotionalcart.product.presentation.dto.ReadProductOptions;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
import com.emotionalcart.product.presentation.dto.ReadProviders;
import com.emotionalcart.product.domain.CategoryDataProvider;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.ProviderDataProvider;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
        private final ProductDataProvider productDataProvider;
        private final CategoryDataProvider categoryDataProvider;
        private final ProviderDataProvider providerDataProvider;

        public Page<ReadProductReviews.Response> readProductReviews(@NotNull Long productId,
                        ReadProductReviews.Request request) {
                productDataProvider.findProduct(productId);

                Page<Review> reviews = productDataProvider.findAllReviews(productId, request.getPageable());
                ReviewImages reviewImages = findAllReviewImages(reviews.getContent());

                return ReadProductReviews.Response.toResponse(reviews, reviewImages);
        }

        private ReviewImages findAllReviewImages(List<Review> reviews) {
                Reviews from = Reviews.from(reviews);
                return ReviewImages.from(productDataProvider.findAllReviewImages(from.ids()));
        }

        public ReadProductDetails.Response getProductDetail(Long productId) {
                // 상품
                Product product = productDataProvider.findProduct(productId);

                // 상품 옵션
                List<ReadProductOptions.Response> productOptionsResponses = new ArrayList<>();

                List<ProductOption> productOptions = productDataProvider.findAllProductOptionsByProductId(productId);

                for (ProductOption productOption : productOptions) {
                        // 상품 옵션 상세
                        List<ProductOptionDetail> productOptionDetails = productDataProvider
                                        .findAllProductOptionDetailsByProductOptionId(productOption.getId());

                        List<ReadProductOptionDetails.Response> productOptionDetailResponses = new ArrayList<>();

                        for (ProductOptionDetail productOptionDetail : productOptionDetails) {
                                // 상품 옵션 상세 이미지 조회
                                List<ProductImage> productImages = productDataProvider
                                                .findAllProductImagesByProductOptionDetailId(
                                                                productOptionDetail.getId());

                                ReadProductOptionDetails.Response productOptionDetailResponse = ReadProductOptionDetails.Response
                                                .toResponse(productOptionDetail, productImages);

                                productOptionDetailResponses.add(productOptionDetailResponse);
                        }

                        ReadProductOptions.Response productOptionResponse = ReadProductOptions.Response
                                        .toResponse(productOption, productOptionDetailResponses);

                        productOptionsResponses.add(productOptionResponse);
                }

                // 카테고리
                ReadProductCategories.Response categoryResponse = ReadProductCategories.Response
                                .toResponse(categoryDataProvider.findCategoryById(product.getCategoryId()));

                // 공급자
                ReadProviders.Response providerResponse = ReadProviders.Response
                                .toResponse(providerDataProvider.findProviderById(product.getProviderId()));

                return ReadProductDetails.Response.toResponse(product, productOptionsResponses, categoryResponse,
                                providerResponse);
        }
}
