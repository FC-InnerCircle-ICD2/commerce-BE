package com.emotionalcart.product.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.emotionalcart.product.domain.entity.ProductCategory;
import lombok.Data;

@Data
public class ReadProductCategoryResponse {

    private Long ProductCategoryId;
    private String name;
    private Boolean isActive;
    private Long parentCategoryId;
    // 재귀적 구조: subCategories 필드도 ReadProductCategoryResponse 타입으로 정의하여, 카테고리의 계층적 구조를 표현합니다.
    private List<ReadProductCategoryResponse> subCategories;

    // ProductCategory 엔티티를 ReadProductCategoryResponse DTO로 변환 (재귀적)
    public static ReadProductCategoryResponse toDto(ProductCategory entity) {
        if (entity == null) {
            return null;
        }

        ReadProductCategoryResponse dto = new ReadProductCategoryResponse();
        dto.setProductCategoryId(entity.getId());
        dto.setName(entity.getName());
        dto.setIsActive(entity.getIsActive());
        dto.setParentCategoryId(entity.getParentProductCategory() != null ? entity.getParentProductCategory().getId() : null);
        dto.setSubCategories(entity.getSubCategories().stream().map(ReadProductCategoryResponse::toDto).collect(Collectors.toList()));
        return dto;
    }
}
