package com.emotionalcart.product.application;

import com.emotionalcart.product.domain.entity.ProductCategory;
import com.emotionalcart.product.domain.repository.ProductCategoryRespository;
import com.emotionalcart.product.presentation.dto.ReadProductCategoryResponse;
import com.emotionalcart.product.presentation.response.Pagination;
import com.emotionalcart.product.presentation.response.Response;
import com.emotionalcart.product.application.dto.request.GetProductReviewsRequest;
import com.emotionalcart.product.application.dto.response.GetProductReviewsResponse;
import com.emotionalcart.product.application.dto.response.ReviewImageResponse;
import com.emotionalcart.product.domain.Review;
import com.emotionalcart.product.domain.ReviewImage;
import com.emotionalcart.product.domain.repository.ProductRepository;
import com.emotionalcart.product.domain.repository.ReviewImageRepository;
import com.emotionalcart.product.domain.repository.ReviewRepository;
import com.emotionalcart.product.presentation.exception.ErrorCode;
import com.emotionalcart.product.presentation.exception.ProductException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    private final ProductCategoryRespository productCategoryRespository;
    public Page<GetProductReviewsResponse> getProductReviews(@NotNull Long productId, GetProductReviewsRequest request) {
        if (!productRepository.existsByIdAndIsDeletedIsFalse(productId)) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }

    public Response<List<ReadProductCategoryResponse>> getAllProductCategories(Pageable pageable){
        Page<Review> reviews = reviewRepository.findAllByProductIdAndIsDeletedIsFalse(
                productId,
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
        Map<Long, List<ReviewImageResponse>> reviewImages = fetchReviewImages(reviews);

        Page<ProductCategory> productCategoryPage = productCategoryRespository.findAll(pageable);
        return reviews.map(review -> {
            List<ReviewImageResponse> images = reviewImages.getOrDefault(review.getId(), List.of());
            return new GetProductReviewsResponse(review, images);
        });
    }

        List<ReadProductCategoryResponse> productCategories = productCategoryPage.getContent()
                .stream()
                .map(ReadProductCategoryResponse::toDto)
                .collect(Collectors.toList());
    private Map<Long, List<ReviewImageResponse>> fetchReviewImages(Page<Review> reviews) {
        List<Long> reviewIds = reviews.getContent().stream()
                                       .map(Review::getId)
                                       .toList();

        Pagination pagination = new Pagination().complete(productCategoryPage, pageable);

        return new Response<List<ReadProductCategoryResponse>>()
                .responseOk(productCategories, pagination);
        return reviewImageRepository.findAllByReviewIdInAndIsDeletedIsFalse(reviewIds).stream()
                       .collect(Collectors.groupingBy(
                               ReviewImage::getReviewId,
                               Collectors.mapping(ReviewImageResponse::new, Collectors.toList())
                       ));
    }
}
