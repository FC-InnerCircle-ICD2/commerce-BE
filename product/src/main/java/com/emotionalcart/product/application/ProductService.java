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
            Long productId = request.getProductId();
            List<ProductDetail> productDetailList = productDetailMap.getOrDefault(productId, List.of());
            Map<Long, Long> optionMap = request.getProductOptions().stream()
                    .collect(Collectors.toMap(
                            ReadProductsValidate.Request.OptionRequest::getProductOptionId,
                            ReadProductsValidate.Request.OptionRequest::getProductOptionDetailId
                    ));

            validateProductOptions(productDetailList, optionMap);
            validateStock(productId, optionMap, request.getQuantity());
        }
    }

    /**
     * 상품 재고가 존재하는지 검증
     */
    private void validateStock(Long productId, Map<Long, Long> option, Integer quantity) {
        StockSearchCondition condition = new StockSearchCondition(productId, option);
        Stock stock = stockDataProvider.findStock(condition);
        if (stock.getQuantity() == null || stock.getQuantity() < quantity) {
            throw new ProductException(ErrorCode.OUT_OF_STOCK);
        }
    }

    public List<ReadProductsPrice.Response> readProductsPrice(List<ReadProductsPrice.Request> requests) {
        Set<Long> productIds = requests.stream()
                .map(ReadProductsPrice.Request::getProductId)
                .collect(Collectors.toSet());

        List<ProductDetail> productDetails = productDataProvider.findAllProductDetail(productIds);
        Map<Long, List<ProductDetail>> productDetailMap = productDetails.stream()
                .collect(Collectors.groupingBy(ProductDetail::getProductId));

        for (ReadProductsPrice.Request request : requests) {
            Long productId = request.getProductId();
            List<ProductDetail> productDetailList = productDetailMap.getOrDefault(productId, List.of());
            Map<Long, Long> optionMap = convertToOptionMap(request.getProductOptions());

            validateProductOptions(productDetailList, optionMap);
        }

        return requests.stream()
                .map(request -> {
                    Long productId = request.getProductId();
                    Map<Long, Long> optionMap = convertToOptionMap(request.getProductOptions());

                    // 선택된 옵션만 필터링하여 가격 반환
                    List<ProductDetail> selectedDetails = productDetailMap.getOrDefault(productId, List.of()).stream()
                            .filter(detail -> optionMap.containsValue(detail.getProductOptionDetailId()))
                            .collect(Collectors.toList());

                    return ReadProductsPrice.toResponse(productId, selectedDetails);
                })
                .collect(Collectors.toList());
    }

    private Map<Long, Long> convertToOptionMap(List<? extends ReadProductsPrice.Request.OptionRequest> options) {
        return options.stream()
                .collect(Collectors.toMap(
                        ReadProductsPrice.Request.OptionRequest::getProductOptionId,
                        ReadProductsPrice.Request.OptionRequest::getProductOptionDetailId
                ));
    }

    private void validateProductOptions(List<ProductDetail> productDetails, Map<Long, Long> optionMap) {
        if (productDetails.isEmpty()) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }
        validateProductOptionCombination(productDetails, optionMap);
        validateAllProductOptionsSelected(productDetails, optionMap);
    }

    /**
     * 요청된 옵션 조합이 상품의 모든 필수 옵션을 포함하는지 검증
     */
    private void validateAllProductOptionsSelected(List<ProductDetail> productDetails, Map<Long, Long> optionMap) {
        Set<Long> requiredOptions = productDetails.stream()
                .map(ProductDetail::getProductOptionId)
                .collect(Collectors.toSet());

        if (!optionMap.keySet().containsAll(requiredOptions)) {
            throw new ProductException(ErrorCode.REQUIRED_OPTION_MISSING);
        }
    }

    /**
     * 특정 상품의 요청된 옵션 조합이 존재하는지 검증
     */
    private void validateProductOptionCombination(List<ProductDetail> productDetails, Map<Long, Long> optionMap) {
        boolean isValid = optionMap.entrySet().stream()
                .allMatch(entry ->
                        productDetails.stream().anyMatch(detail ->
                                detail.getProductOptionId().equals(entry.getKey()) &&
                                        detail.getProductOptionDetailId().equals(entry.getValue()))
                );

        if (!isValid) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION);
        }
    }

}
