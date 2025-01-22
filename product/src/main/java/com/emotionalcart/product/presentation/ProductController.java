package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.ProductService;
import com.emotionalcart.product.presentation.dto.ReadCategories;
import com.emotionalcart.product.presentation.dto.ReadProductDetails;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
//import com.emotionalcart.product.presentation.dto.ReadProductDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController implements ProductControllerDocs {
    private final ProductService productService;

    // 상품 리뷰 조회
    @Override
    public ResponseEntity<Page<ReadProductReviews.Response>> readProductReviews(
            @PathVariable Long productId,
            ReadProductReviews.Request request) {
        return ResponseEntity.ok(productService.readProductReviews(productId, request));
    }

    // 상품 상세 조회
    @Override
    public ResponseEntity<ReadProductDetails.Response> getProductDetail(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }
}
