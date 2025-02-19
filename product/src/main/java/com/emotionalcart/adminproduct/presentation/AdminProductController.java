package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.application.AdminProductService;
import com.emotionalcart.adminproduct.presentation.dto.CreateProductRequest;
import com.emotionalcart.adminproduct.presentation.dto.CreateProductResponse;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProductDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/products")
@RequiredArgsConstructor
public class AdminProductController implements AdminProductControllerDocs {
    private final AdminProductService adminProductService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateProductResponse> createProduct(
            @Valid @ModelAttribute CreateProductRequest request) {
        return ResponseEntity.ok(adminProductService.createProduct(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ReadAdminProductDetailResponse> readProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(adminProductService.readProduct(productId));
    }
}
