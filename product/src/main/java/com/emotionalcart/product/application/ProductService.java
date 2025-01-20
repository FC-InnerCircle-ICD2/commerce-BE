package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.presentation.dto.ReadCategories;
import com.emotionalcart.product.presentation.dto.ReadProductDetails;
import com.emotionalcart.product.presentation.dto.ReadProductImages;
import com.emotionalcart.product.presentation.dto.ReadProductOptionDetails;
import com.emotionalcart.product.presentation.dto.ReadProductOptions;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
import com.emotionalcart.product.presentation.dto.ReadProviders;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;

//import com.emotionalcart.product.presentation.dto.ReadProductDetail;
//import com.emotionalcart.product.domain.Product;
//import com.emotionalcart.product.domain.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDataProvider productDataProvider;

    public List<ReadCategories.Response> getAllProductCategories() {
        List<Category> categories = productDataProvider.findAllCategories();

        return ReadCategories.Response.toResponse(categories);
    }

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
                        .findAllProductImagesByProductOptionDetailId(productOptionDetail.getId());

                ReadProductOptionDetails.Response productOptionDetailResponse = ReadProductOptionDetails.Response
                        .toResponse(productOptionDetail, productImages);

                productOptionDetailResponses.add(productOptionDetailResponse);
            }

            ReadProductOptions.Response productOptionResponse = ReadProductOptions.Response
                    .toResponse(productOption, productOptionDetailResponses);

            productOptionsResponses.add(productOptionResponse);
        }

        // 카테고리
        Long categoryId = productDataProvider.findCategoryIdByProductId(productId);

        ReadCategories.Response categoryResponse = ReadCategories.Response
                .toResponse(productDataProvider.findCategoryById(categoryId));

        // 공급자
        Long providerId = productDataProvider.findProviderIdByProductId(productId);

        ReadProviders.Response providerResponse = ReadProviders.Response
                .toResponse(productDataProvider.findProviderById(providerId));

        return ReadProductDetails.Response.toResponse(product, productOptionsResponses, categoryResponse,
                providerResponse);
    }
}
