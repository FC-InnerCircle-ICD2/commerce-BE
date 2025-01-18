package com.emotionalcart.product.application;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.application.dto.ReadCategories;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
import com.emotionalcart.product.presentation.dto.ReadProductValidate;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductDataProvider productDataProvider;

    public List<ReadCategories.Response> getAllProductCategories() {
        List<Category> categories = productDataProvider.findAllCategories();

        return ReadCategories.Response.fromCategories(categories);
    }

    public Page<ReadProductReviews.Response> readProductReviews(@NotNull Long productId, ReadProductReviews.Request request) {
        productDataProvider.findProduct(productId);

        Page<Review> reviews = productDataProvider.findAllReviews(productId, request.getPageable());
        ReviewImages reviewImages = findAllReviewImages(reviews.getContent());

        return ReadProductReviews.Response.toResponse(reviews, reviewImages);
    }

    private ReviewImages findAllReviewImages(List<Review> reviews) {
        Reviews from = Reviews.from(reviews);
        return ReviewImages.from(productDataProvider.findAllReviewImages(from.ids()));
    }

    public void readProductValidate(List<ReadProductValidate.Request> requests) {
        Set<Long> productIds = requests.stream()
                .map(ReadProductValidate.Request::getProductId)
                .collect(Collectors.toSet());

        List<ProductDetail> productDetails = productDataProvider.findAllProductData(productIds);
        Map<Long, List<ProductDetail>> groupedProductDetail = productDetails.stream()
                .collect(Collectors.groupingBy(ProductDetail::getProductId));

        for (ReadProductValidate.Request request : requests) {
            List<ProductDetail> productDetailForProduct = groupedProductDetail.get(request.getProductId());

            if (productDetailForProduct == null || productDetailForProduct.isEmpty()) {
                throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
            }

            if (!validateOptions(productDetailForProduct, request)) {
                throw new ProductException(ErrorCode.REQUIRED_OPTION_MISSING);
            }

            if (!validateQuantity(productDetailForProduct, request)) {
                throw new ProductException(ErrorCode.OUT_OF_STOCK);
            }
        }
    }

    private boolean validateOptions(List<ProductDetail> productDetails, ReadProductValidate.Request request) {
        Set<Long> allOptionIds = productDetails.stream()
                .map(ProductDetail::getProductOptionId)
                .collect(Collectors.toSet());

        Set<Long> requiredOptionIds = productDetails.stream()
                .filter(ProductDetail::isRequired)
                .map(ProductDetail::getProductOptionId)
                .collect(Collectors.toSet());

        Set<Long> selectedOptionIds = request.getProductOptions().stream()
                .map(ReadProductValidate.Request.OptionRequest::getProductOptionId)
                .collect(Collectors.toSet());

        if (!allOptionIds.containsAll(selectedOptionIds)) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION);
        }

        return selectedOptionIds.containsAll(requiredOptionIds);
    }

    private boolean validateQuantity(List<ProductDetail> productDetails, ReadProductValidate.Request request) {
        Map<Long, Integer> optionDetailStockMap = productDetails.stream()
                .collect(Collectors.toMap(ProductDetail::getProductOptionDetailId, ProductDetail::getQuantity));

        for (ReadProductValidate.Request.OptionRequest option : request.getProductOptions()) {
            if (!optionDetailStockMap.containsKey(option.getProductOptionDetailId())) {
                throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION);
            }
            Integer availableStock = optionDetailStockMap.get(option.getProductOptionDetailId());
            if (availableStock == null || option.getQuantity() > availableStock) {
                return false;
            }
        }

        return true;
    }
}
