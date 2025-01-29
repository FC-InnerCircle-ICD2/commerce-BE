package com.emotionalcart.product.application;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.stock.Stock;
import com.emotionalcart.product.domain.CategoryDataProvider;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.ProviderDataProvider;
import com.emotionalcart.product.domain.StockDataProvider;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.domain.support.ProductDetails;
import com.emotionalcart.product.domain.support.ReviewImages;
import com.emotionalcart.product.domain.support.Reviews;
import com.emotionalcart.product.infrastructure.StockSearchCondition;
import com.emotionalcart.product.presentation.dto.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductDataProvider productDataProvider;
    private final CategoryDataProvider categoryDataProvider;
    private final ProviderDataProvider providerDataProvider;
    private final StockDataProvider stockDataProvider;

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

    public void readProductsValidate(List<ReadProductsValidate.Request> requests) {
        Set<Long> productIds = requests.stream()
                .map(ReadProductsValidate.Request::getProductId)
                .collect(Collectors.toSet());

        List<ProductDetail> productDetails = productDataProvider.findAllProductDetail(productIds);
        Map<Long, List<ProductDetail>> productDetailMap = productDetails.stream()
                .collect(Collectors.groupingBy(ProductDetail::getProductId));

        for (ReadProductsValidate.Request request : requests) {
            Map<Long, Long> optionMap = request.getProductOptions().stream()
                    .collect(Collectors.toMap(
                            ReadProductsValidate.Request.OptionRequest::getProductOptionId,
                            ReadProductsValidate.Request.OptionRequest::getProductOptionDetailId
                    ));

            // 상품이 존재하는지 검증
            if (!productDetailMap.containsKey(request.getProductId())) {
                throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
            }
            // 옵션 조합이 유효한지 검증
            validateProductOptionCombination(productDetailMap.get(request.getProductId()), optionMap);
            // 재고 검증
            validateStock(request.getProductId(), optionMap, request.getQuantity());
        }
    }

    private void validateProductOptionCombination(List<ProductDetail> productDetails, Map<Long, Long> optionMap) {
        for (Map.Entry<Long, Long> entry : optionMap.entrySet()) {
            Long optionId = entry.getKey();
            Long optionDetailId = entry.getValue();

            boolean isValid = productDetails.stream()
                    .anyMatch(detail ->
                            detail.getProductOptionId().equals(optionId) &&
                                    detail.getProductOptionDetailId().equals(optionDetailId)
                    );

            if (!isValid) {
                throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION);
            }
        }
    }

    private void validateStock(Long productId, Map<Long, Long> option, Integer quantity) {
        StockSearchCondition condition = new StockSearchCondition(productId, option);
        Stock stock = stockDataProvider.findStock(condition);
        if (stock.getQuantity() < quantity) {
            throw new ProductException(ErrorCode.OUT_OF_STOCK);
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
                            Collectors.mapping(ProductDetail::getProductOptionDetailId, Collectors.toSet())
                    ));

            for (ReadProductsPrice.Request.OptionRequest option : request.getProductOptions()) {
                Set<Long> validDetailIds = optionToDetailMap.get(option.getProductOptionId());
                if (validDetailIds.isEmpty() || !validDetailIds.contains(option.getProductOptionDetailId())) {
                    throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION);
                }
            }
        }
    }
}
