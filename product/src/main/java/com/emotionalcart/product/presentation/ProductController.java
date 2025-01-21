package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.ProductService;
import com.emotionalcart.product.presentation.dto.ReadCategories;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
//import com.emotionalcart.product.presentation.dto.ReadProductDetail;

import com.emotionalcart.product.presentation.dto.ReadProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<Page<ReadProductReviews.Response>> readProductReviews(
            @PathVariable Long productId,
            ReadProductReviews.Request request) {
        return ResponseEntity.ok(productService.readProductReviews(productId, request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ReadCategories.Response>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllProductCategories());
    }

    // 상품 상세 조회
//    @GetMapping("/{productId}")
//    public ResponseEntity<ReadProductDetail.Response> getProductDetail(@PathVariable Long productId) {
//        return ResponseEntity.ok(productService.getProductDetail(productId));
//    }

    @PostMapping
    public ResponseEntity<Page<ReadProducts.Response>> readProducts(
            @RequestBody ReadProducts.Request request
    ) {
        // 정렬 설정
        request.configureSort();

        return ResponseEntity.ok(productService.readProducts(request));
    }
}
