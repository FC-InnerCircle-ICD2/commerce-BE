package com.emotionalcart.product.presentation.controller;

import com.emotionalcart.product.application.ProductService;
import com.emotionalcart.product.domain.entity.ProductCategory;
import com.emotionalcart.product.presentation.dto.ReadProductCategoryResponse;
import com.emotionalcart.product.presentation.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping(value="/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/category")
    public Response<List<ReadProductCategoryResponse>> getAllCategories(Pageable pageable) {
        return productService.getAllProductCategories(pageable);
    }
}
