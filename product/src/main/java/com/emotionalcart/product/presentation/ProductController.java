package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.ProductService;
import com.emotionalcart.product.presentation.dto.ReadProductStock;
import jakarta.validation.Valid;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
import com.emotionalcart.product.application.dto.ReadCategories;

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
            ReadProductReviews.Request request
    ) {
        return ResponseEntity.ok(productService.readProductReviews(productId, request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ReadCategories.Response>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllProductCategories());
    }

    @PostMapping("/stock")
    public ResponseEntity<List<ReadProductStock.Response>> readProductStock(
            @RequestBody @Valid List<ReadProductStock.Request> request
    ) {
        return ResponseEntity.ok(productService.readProductStock(request));
    }
}
