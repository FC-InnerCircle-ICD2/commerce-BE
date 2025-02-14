package com.emotionalcart.adminproduct.domain;

import com.emotionalcart.adminproduct.infrastructure.repository.AdminCategoryRepository;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCategoryDataProvider {
    private final AdminCategoryRepository categoryRepository;

    public void validateCategory(Long categoryId) {
        Category category = categoryRepository.findByIdAndIsDeletedIsFalse(categoryId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_CATEGORY));
        if (category.getDepth() != 2) {
            throw new ProductException(ErrorCode.CATEGORY_MUST_BE_DEPTH_2);
        }
    }
}