package com.emotionalcart.product.application;

import com.emotionalcart.common.security.LoginAccountAuditorAware;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.provider.Provider;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.core.feature.review.ReviewImage;
import com.emotionalcart.product.domain.CategoryDataProvider;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.ProviderDataProvider;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.domain.support.*;
import com.emotionalcart.product.presentation.dto.*;
import com.emotionalcart.product.presentation.dto.request.CreateProductReviewRequest;
import com.emotionalcart.product.presentation.dto.response.CreateProductReviewResponse;
import com.emotionalcart.s3.S3Utils;
import com.emotionalcart.s3.config.S3Constants;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final S3Utils s3Utils;
    private final LoginAccountAuditorAware loginAccountAuditorAware;

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

    @Transactional
    public CreateProductReviewResponse createProductReview(@NotNull Long productId, CreateProductReviewRequest request) {
        String userId = loginAccountAuditorAware.getCurrentAuditor()
            .orElseThrow(() -> new ProductException(ErrorCode.UNAUTHORIZE_ERROR))
            .toString();

        Product product = productDataProvider.findProduct(productId);
        productDataProvider.findProductReview(productId, userId);
        // TODO 유저 구매내역 확인
        Review review = request.toReviewEntity(productId);
        productDataProvider.saveProductReview(review);

        List<ReviewImage> reviewImages = uploadAndCreateReviewImages(review.getId(), request.getReviewImages());
        productDataProvider.saveProductReviewImages(reviewImages);

        product.getReviewStatistic().updateStatistics(request.getRating());
        return new CreateProductReviewResponse(review.getId());
    }

    /**
     * 리뷰 이미지 entity 생성 및 s3 저장
     */
    private List<ReviewImage> uploadAndCreateReviewImages(Long reviewId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        return files.stream().map(file -> {
            try {
                String fileUrl = s3Utils.uploadFile(S3Constants.REVIEW_DIRECTORY, reviewId.toString(), file);
                return ReviewImage.of(
                    reviewId,
                    file.getOriginalFilename(),
                    fileUrl,
                    file.getContentType(),
                    file.getSize(),
                    files.indexOf(file) + 1
                );
            } catch (Exception e) {
                throw new ProductException(ErrorCode.S3_UPLOAD_FAILED);
            }
        }).toList();
    }

    public Page<ReadProducts.Response> readProducts(ReadProducts.Request request) {
        Page<Product> productPage = productDataProvider.findAllProducts(request.toProductSearch());

        Products products = Products.from(productPage);

        ProductOptions productOptions = ProductOptions.from(productDataProvider.findProductOptions(products.ids()));
        Map<Long, Category> categories = categoryDataProvider.findCategoryByIds(products.getCategoryIds());
        Map<Long, Provider> providers = providerDataProvider.findProviderByIds(products.getProviderIds());
        ProductImages productImages = ProductImages.from(productDataProvider.findAllProductImages(products.ids()));

        // DTO 변환
        return ReadProducts.Response.toResponse(productPage, productOptions, categories, providers, productImages);
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
                ReadProductOptionDetails.Response productOptionDetailResponse =
                    ReadProductOptionDetails.Response.toResponse(productOptionDetail);
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

        // 상품 이미지
        List<ReadProductImages.Response> productImages = ReadProductImages.Response
            .toResponse(productDataProvider.findProductImages(productId));

        return ReadProductDetails.Response.toResponse(product, productOptionsResponses, categoryResponse,
                                                      providerResponse, reviewStatistic, productImages);
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
        }

        // TODO 재고 검증
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
            .map(entry -> {
                Long productId = entry.getKey();
                List<ProductDetail> details = entry.getValue();
                Long providerId = details.stream()
                    .map(ProductDetail::getProviderId)
                    .findFirst()
                    .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT));
                return ReadProductsPrice.toResponse(productId, providerId, details);
            })
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
