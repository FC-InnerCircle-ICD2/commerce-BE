package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.application.AdminProductService;
import com.emotionalcart.adminproduct.presentation.dto.CreateProduct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/products")
@RequiredArgsConstructor
public class AdminProductController implements AdminProductControllerDocs {
    private final AdminProductService adminProductService;

    @PostMapping
    public ResponseEntity<CreateProduct.Response> createProduct(
            @Valid @ModelAttribute CreateProduct.Request request) {
        return ResponseEntity.ok(adminProductService.createProduct(request));
    }
}
