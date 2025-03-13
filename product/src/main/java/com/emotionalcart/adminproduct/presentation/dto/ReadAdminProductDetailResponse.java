package com.emotionalcart.adminproduct.presentation.dto;

import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.*;
import com.emotionalcart.core.feature.provider.Provider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadAdminProductDetailResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private ReadAdminProductCategoryResponse category;
    private ReadAdminProviderResponse provider;
    private List<ReadAdminProductOptionResponse> options;
    private List<ReadAdminProductImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReadAdminProductDetailResponse(Product product, Category category, Provider provider) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.category = ReadAdminProductCategoryResponse.toResponse(category);
        this.provider = ReadAdminProviderResponse.toResponse(provider);
        this.options = product.getOptions().stream()
            .filter(option -> !option.getIsDeleted())
            .map(ReadAdminProductOptionResponse::toResponse)
            .collect(Collectors.toList());
        this.images = product.getImages().stream()
            .filter(image -> !image.getIsDeleted())
            .sorted(Comparator.comparing(ProductImage::getImageType)
                        .thenComparing(ProductImage::getFileOrder))
            .map(ReadAdminProductImageResponse::toResponse)
            .collect(Collectors.toList());
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    public static ReadAdminProductDetailResponse toResponse(Product product, Category category, Provider provider) {
        return new ReadAdminProductDetailResponse(product, category, provider);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ReadAdminProductCategoryResponse {

    private Long id;
    private String name;
    private Long parentCategoryId;
    private String parentCategoryName;

    public ReadAdminProductCategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parentCategoryId = category.getParentCategory().getId();
        this.parentCategoryName = category.getParentCategory().getName();
    }

    public static ReadAdminProductCategoryResponse toResponse(Category category) {
        return new ReadAdminProductCategoryResponse(category);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ReadAdminProviderResponse {

    private Long id;
    private String name;

    public ReadAdminProviderResponse(Provider provider) {
        this.id = provider.getId();
        this.name = provider.getName();
    }

    public static ReadAdminProviderResponse toResponse(Provider provider) {
        return new ReadAdminProviderResponse(provider);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ReadAdminProductOptionResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private List<ReadProductOptionDetailResponse> optionDetails;

    public ReadAdminProductOptionResponse(ProductOption option) {
        this.id = option.getId();
        this.name = option.getName();
        this.optionDetails = option.getDetails().stream()
            .filter(detail -> !detail.getIsDeleted())
            .map(ReadProductOptionDetailResponse::new)
            .collect(Collectors.toList());
    }

    public static ReadAdminProductOptionResponse toResponse(ProductOption option) {
        return new ReadAdminProductOptionResponse(option);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ReadProductOptionDetailResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String value;
    private Integer quantity;
    private Integer order;
    private Integer additionalPrice;

    public ReadProductOptionDetailResponse(ProductOptionDetail optionDetail) {
        this.id = optionDetail.getId();
        this.value = optionDetail.getValue();
        this.quantity = optionDetail.getQuantity();
        this.order = optionDetail.getOptionOrder();
        this.additionalPrice = optionDetail.getAdditionalPrice();
    }

    public static ReadProductOptionDetailResponse toResponse(ProductOptionDetail optionDetail) {
        return new ReadProductOptionDetailResponse(optionDetail);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ReadAdminProductImageResponse {

    private Long id;
    private String imageUrl;
    private ProductImageType productImageType;
    private Integer fileOrder;

    public ReadAdminProductImageResponse(ProductImage image) {
        this.id = image.getId();
        this.imageUrl = image.getFilePath();
        this.productImageType = image.getImageType();
        this.fileOrder = image.getFileOrder();
    }

    public static ReadAdminProductImageResponse toResponse(ProductImage image) {
        return new ReadAdminProductImageResponse(image);
    }

}