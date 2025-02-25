package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.application.AdminProductService;
import com.emotionalcart.adminproduct.presentation.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/v1/products")
@RequiredArgsConstructor
public class AdminProductController implements AdminProductControllerDocs {

    private final AdminProductService adminProductService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateProductResponse> createProduct(
        @Valid @ModelAttribute CreateProductRequest request) {
        return ResponseEntity.ok(adminProductService.createProduct(request));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ReadAdminProductsResponse>> readProducts(ReadAdminProductsRequest request) {
        return ResponseEntity.ok(adminProductService.readProducts(request));
    }

    @Override
    @GetMapping("/{productId}")
    public ResponseEntity<ReadAdminProductDetailResponse> readProduct(
        @PathVariable Long productId) {
        return ResponseEntity.ok(adminProductService.readProduct(productId));
    }

    @Override
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        adminProductService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @Valid @RequestBody UpdateProductRequest request) {
        adminProductService.updateProduct(productId, request);
        return ResponseEntity.ok().build();
    }

}
