package com.emotionalcart.product.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.emotionalcart.core.feature.category.Category;

import lombok.Data;

public class ReadCategories {

    @Data
    public static class Response {
        private Long productCategoryId;
        private String name;
        private Long parentProductCategoryId;
        private List<Response> subProductCategories;

        // Constructor to map Category to Response
        public Response(Category category) {
            this.productCategoryId = category.getId();
            this.name = category.getName();
            this.parentProductCategoryId = category.getParentCategory() != null ? category.getParentCategory().getId()
                    : null;
            this.subProductCategories = category.getSubCategories().stream()
                    .filter(Category::getIsActive)
                    .map(Response::new)
                    .collect(Collectors.toList());
        }

        // Converts a list of Category objects to a list of Response objects
        // Only top-level categories (depth == 1) are included
        public static List<Response> toResponse(List<Category> categories) {
            return categories.stream()
                    .filter(category -> category.getDepth() == 1)
                    .map(Response::new)
                    .collect(Collectors.toList());
        }

        public static Response toResponse(Category category) {
            return new Response(category);
        }
    }
}
