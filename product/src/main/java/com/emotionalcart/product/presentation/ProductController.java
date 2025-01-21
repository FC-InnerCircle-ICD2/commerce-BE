package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.ProductService;
import com.emotionalcart.product.presentation.dto.ReadCategories;
import jakarta.validation.Valid;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.emotionalcart.product.presentation.dto.ReadProductValidate;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<Page<ReadProductReviews.Response>> readProductReviews(
            @PathVariable Long productId,
            ReadProductReviews.Request request
    ) {
        return ResponseEntity.ok(productService.readProductReviews(productId, request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ReadCategories.Response>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllProductCategories());
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> readProductValidate(
            @RequestBody @Valid List<ReadProductValidate.Request> request
    ) {
        productService.readProductValidate(request);
        return ResponseEntity.ok().build();
    }
}
