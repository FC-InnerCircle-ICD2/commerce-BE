package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.ProductService;
import com.emotionalcart.product.presentation.dto.*;
import com.emotionalcart.product.presentation.dto.request.CreateProductReviewRequest;
import com.emotionalcart.product.presentation.dto.response.CreateProductReviewResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController implements ProductControllerDocs {
    private final ProductService productService;

    // 상품 리뷰 조회
    @Override
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<Page<ReadProductReviews.Response>> readProductReviews(
            @PathVariable Long productId,
            ReadProductReviews.Request request) {
        return ResponseEntity.ok(productService.readProductReviews(productId, request));
    }

    // 상품 리뷰 등록
    @Override
    @PostMapping(value = "/{productId}/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateProductReviewResponse> createProductReview(
            @PathVariable Long productId,
            @ModelAttribute @Valid CreateProductReviewRequest request) {
        return ResponseEntity.ok(productService.createProductReview(productId, request));
    }

    // 상품 상세 조회
    @Override
    public ResponseEntity<ReadProductDetails.Response> getProductDetail(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }

    @Override
    public ResponseEntity<Page<ReadProducts.Response>> readProducts(
            ReadProducts.Request request) {
        return ResponseEntity.ok(productService.readProducts(request));
    }

    @Override
    public ResponseEntity<Void> readProductsValidate(
            @RequestBody @Valid List<ReadProductsValidate.Request> requests) {
        productService.readProductsValidate(requests);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ReadProductsPrice.Response>> readProductsPrice(
            @RequestBody @Valid List<ReadProductsPrice.Request> requests) {
        return ResponseEntity.ok(productService.readProductsPrice(requests));
    }
}
