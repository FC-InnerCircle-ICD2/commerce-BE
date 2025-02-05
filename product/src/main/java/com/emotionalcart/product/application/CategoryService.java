package com.emotionalcart.product.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.product.domain.CategoryDataProvider;
import com.emotionalcart.product.presentation.dto.ReadCategories;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

private final CategoryDataProvider categoryDataProvider;

    public List<ReadCategories.Response> getAllProductCategories() {
        List<Category> categories = categoryDataProvider.findAllCategories();

        return ReadCategories.Response.toResponse(categories);
    }
    
}
