package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.CategoryService;
import com.emotionalcart.product.presentation.dto.ReadCategories;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<List<ReadCategories.Response>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllProductCategories());
    }
}
