package com.emotionalcart.product.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.product.infrastructure.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryDataProvider {
    private final CategoryRepository categoryRepository;

    public List<Category> findAllCategories() {
        return categoryRepository.findAllByIsActiveIsTrueAndIsDeletedIsFalse();
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findByIdAndIsDeletedIsFalse(categoryId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_CATEGORY));
    }
}
