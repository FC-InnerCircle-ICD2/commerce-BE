package com.emotionalcart.product.application;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.presentation.dto.ReadCategories;
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
        ProductDetails groupedProductDetails = ProductDetails.from(productDetails);

        for (ReadProductValidate.Request request : requests) {
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

    private void validateOptions(ProductDetails groupedProductDetails, ReadProductValidate.Request request) {
        Long productId = request.getProductId();
        Set<Long> allOptionIds = groupedProductDetails.getAllOptionIds(productId);
        Set<Long> allOptionDetailIds = groupedProductDetails.getAllOptionDetailIds(productId);
        Set<Long> requiredOptionIds = groupedProductDetails.getRequiredOptionIds(productId);
        Set<Long> selectedOptionIds = request.getProductOptions().stream()
                .map(ReadProductValidate.Request.OptionRequest::getProductOptionId)
                .collect(Collectors.toSet());
        Set<Long> selectedOptionDetailIds = request.getProductOptions().stream()
                .map(ReadProductValidate.Request.OptionRequest::getProductOptionDetailId)
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

    private void validateStock(ProductDetails groupedProductDetails, ReadProductValidate.Request request) {
        List<ProductDetail> productDetails = groupedProductDetails.getDetailsByProductId(request.getProductId());
        Map<Long, Integer> optionDetailStockMap = productDetails.stream()
                .collect(Collectors.toMap(ProductDetail::getProductOptionDetailId, ProductDetail::getQuantity));

        for (ReadProductValidate.Request.OptionRequest option : request.getProductOptions()) {
            Integer availableStock = optionDetailStockMap.get(option.getProductOptionDetailId());
            if (availableStock == null || option.getQuantity() > availableStock) {
                throw new ProductException(ErrorCode.OUT_OF_STOCK);
            }
        }
    }
}
