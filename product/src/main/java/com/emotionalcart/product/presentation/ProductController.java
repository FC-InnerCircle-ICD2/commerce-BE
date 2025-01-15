package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.ProductService;
import com.emotionalcart.product.application.dto.GetProductReviews;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<Page<GetProductReviews.Response>> getProductReviews(
            @PathVariable Long productId,
            GetProductReviews.Request request
    ) {
        return ResponseEntity.ok(productService.getProductReviews(productId, request));
    }
}
