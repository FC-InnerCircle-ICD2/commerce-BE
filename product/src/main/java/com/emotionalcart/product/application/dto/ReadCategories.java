package com.emotionalcart.product.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.emotionalcart.core.feature.category.Category;

import lombok.Data;

public class ReadCategories {

    @Data
    public static class Response {
        private Long id;
        private String name;
        private Long parentCategoryId;
        private List<Response> subCategories;

        // Constructor to map Category to Response
        public Response(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.parentCategoryId = category.getParentCategory() != null ? category.getParentCategory().getId() : null;
            this.subCategories = category.getSubCategories().stream()
                    .filter(Category::getIsActive)
                    .map(Response::new)
                    .collect(Collectors.toList());
        }

        // Converts a list of Category objects to a list of Response objects
        // Only top-level categories (depth == 1) are included
        public static List<Response> fromCategories(List<Category> categories) {
            return categories.stream()
                    .filter(category -> category.getDepth() == 1)
                    .map(Response::new)
                    .collect(Collectors.toList());
        }
    }
}
