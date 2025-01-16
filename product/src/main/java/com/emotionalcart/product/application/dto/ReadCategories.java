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

        public Response(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.parentCategoryId = category.getParentCategory() != null ? category.getParentCategory().getId() : null;
            this.subCategories = category.getSubCategories().stream().map(Response::new).collect(Collectors.toList());
        }

        public static List<Response> toResponse(List<Category> categories) {
            return categories.stream().map(Response::new).collect(Collectors.toList());
        }

    }
}
