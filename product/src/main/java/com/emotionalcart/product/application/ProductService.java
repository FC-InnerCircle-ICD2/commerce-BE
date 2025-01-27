package com.emotionalcart.product.application;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.domain.support.ProductDetails;
import com.emotionalcart.product.domain.support.ReviewImages;
import com.emotionalcart.product.domain.support.Reviews;
import com.emotionalcart.product.presentation.dto.ReadCategories;
import com.emotionalcart.product.presentation.dto.ReadProductCategories;
import com.emotionalcart.product.presentation.dto.ReadProductDetails;
import com.emotionalcart.product.presentation.dto.ReadProductOptionDetails;
import com.emotionalcart.product.presentation.dto.ReadProductOptions;
import com.emotionalcart.product.presentation.dto.ReadProductReviewStatistic;
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

import com.emotionalcart.product.presentation.dto.ReadProductsPrice;
import com.emotionalcart.product.presentation.dto.ReadProductsValidate;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

        // 상품 정보
        Product product = productDataProvider.findProduct(productId);

        // 상품 옵션 정보
        List<ReadProductOptions.Response> productOptionsResponses = new ArrayList<>();

        List<ProductOption> productOptions = productDataProvider.findAllProductOptionsByProductId(productId);

        for (ProductOption productOption : productOptions) {
            // 상품 옵션 상세 정보
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

        // 리뷰 평균 평점 및 리뷰 개수
        ReadProductReviewStatistic.Response reviewStatistic = ReadProductReviewStatistic.Response
                .toResponse(productDataProvider.findReviewStatistic(productId));

        // 카테고리 정보
        ReadProductCategories.Response categoryResponse = ReadProductCategories.Response
                .toResponse(categoryDataProvider.findCategoryById(product.getCategoryId()));

        // 공급자 정보
        ReadProviders.Response providerResponse = ReadProviders.Response
                .toResponse(providerDataProvider.findProviderById(product.getProviderId()));

        return ReadProductDetails.Response.toResponse(product, productOptionsResponses, categoryResponse,
                providerResponse, reviewStatistic);
    }

    public void readProductsValidate(List<ReadProductsValidate.Request> requests) {
        Set<Long> productIds = requests.stream()
                .map(ReadProductsValidate.Request::getProductId)
                .collect(Collectors.toSet());

        List<ProductDetail> productDetails = productDataProvider.findAllProductDetail(productIds);
        ProductDetails groupedProductDetails = ProductDetails.from(productDetails);

        for (ReadProductsValidate.Request request : requests) {
            validateProductExists(groupedProductDetails, request.getProductId());
            validateOptions(groupedProductDetails, request);
            validateStock(groupedProductDetails, request);
        }
    }

    private void validateProductExists(ProductDetails groupedProductDetails, Long productId) {
        if (groupedProductDetails.getDetailsByProductId(productId).isEmpty()) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }
    }

    private void validateOptions(ProductDetails groupedProductDetails, ReadProductsValidate.Request request) {
        Long productId = request.getProductId();
        Set<Long> allOptionIds = groupedProductDetails.getAllOptionIds(productId);
        Set<Long> allOptionDetailIds = groupedProductDetails.getAllOptionDetailIds(productId);
        Set<Long> requiredOptionIds = groupedProductDetails.getRequiredOptionIds(productId);
        Set<Long> selectedOptionIds = request.getProductOptions().stream()
                .map(ReadProductsValidate.Request.OptionRequest::getProductOptionId)
                .collect(Collectors.toSet());
        Set<Long> selectedOptionDetailIds = request.getProductOptions().stream()
                .map(ReadProductsValidate.Request.OptionRequest::getProductOptionDetailId)
                .collect(Collectors.toSet());

        if (!allOptionIds.containsAll(selectedOptionIds)) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION);
        }
        if (!allOptionDetailIds.containsAll(selectedOptionDetailIds)) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION);
        }
        if (!selectedOptionIds.containsAll(requiredOptionIds)) {
            throw new ProductException(ErrorCode.REQUIRED_OPTION_MISSING);
        }
    }

    private void validateStock(ProductDetails groupedProductDetails, ReadProductsValidate.Request request) {
        List<ProductDetail> productDetails = groupedProductDetails.getDetailsByProductId(request.getProductId());
        Map<Long, Integer> optionDetailStockMap = productDetails.stream()
                .collect(Collectors.toMap(ProductDetail::getProductOptionDetailId, ProductDetail::getQuantity));

        for (ReadProductsValidate.Request.OptionRequest option : request.getProductOptions()) {
            Integer availableStock = optionDetailStockMap.get(option.getProductOptionDetailId());
            if (availableStock == null || option.getQuantity() > availableStock) {
                throw new ProductException(ErrorCode.OUT_OF_STOCK);
            }
        }
    }

    public List<ReadProductsPrice.Response> readProductsPrice(List<ReadProductsPrice.Request> requests) {
        Set<Long> productIds = requests.stream()
                .map(ReadProductsPrice.Request::getProductId)
                .collect(Collectors.toSet());

        List<ProductDetail> productDetails = productDataProvider.findAllProductDetail(productIds);
        Set<Long> productOptionDetailIds = requests.stream()
                .flatMap(request -> request.getProductOptions().stream())
                .map(ReadProductsPrice.Request.OptionRequest::getProductOptionDetailId)
                .collect(Collectors.toSet());

        ProductDetails groupedProductDetails = ProductDetails.from(productDetails);
        List<ProductDetail> filteredDetails = groupedProductDetails.filterByOptionDetailIds(productOptionDetailIds);

        validateOptions(groupedProductDetails, requests);

        return filteredDetails.stream()
                .collect(Collectors.groupingBy(ProductDetail::getProductId))
                .entrySet().stream()
                .map(entry -> ReadProductsPrice.toResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private void validateOptions(ProductDetails groupedProductDetails, List<ReadProductsPrice.Request> requests) {
        for (ReadProductsPrice.Request request : requests) {
            Long productId = request.getProductId();
            List<ProductDetail> productDetails = groupedProductDetails.getDetailsByProductId(productId);

            if (productDetails.isEmpty()) {
                throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
            }

            Map<Long, Set<Long>> optionToDetailMap = productDetails.stream()
                    .collect(Collectors.groupingBy(
                            ProductDetail::getProductOptionId,
                            Collectors.mapping(ProductDetail::getProductOptionDetailId, Collectors.toSet())));

            for (ReadProductsPrice.Request.OptionRequest option : request.getProductOptions()) {
                Set<Long> validDetailIds = optionToDetailMap.get(option.getProductOptionId());
                if (validDetailIds.isEmpty() || !validDetailIds.contains(option.getProductOptionDetailId())) {
                    throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION);
                }
            }
        }
    }
}
