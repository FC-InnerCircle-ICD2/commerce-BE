package com.emotionalcart.core.feature.product;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.review.ReviewStatistic;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer price;

    private Long providerId;

    private Long categoryId;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ReviewStatistic reviewStatistic;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductOption> options;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImage> images = new ArrayList<>();

    private Product(
        String name,
        String description,
        Integer price,
        Long providerId,
        Long categoryId
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.providerId = providerId;
        this.categoryId = categoryId;
    }

    public static Product of(
        String name,
        String description,
        Integer price,
        Long providerId,
        Long categoryId
    ) {
        return new Product(
            name,
            description,
            price,
            providerId,
            categoryId
        );
    }

    public void setReviewStatistic(ReviewStatistic reviewStatistic) {
        this.reviewStatistic = reviewStatistic;
        reviewStatistic.setProduct(this);
    }

    public void setOptions(List<ProductOption> options) {
        this.options = options != null ? options : new ArrayList<>();
        for (ProductOption option : this.options) {
            option.setProduct(this);
        }
    }

    public ProductOption addOption(ProductOption option) {
        this.options.add(option);
        option.setProduct(this);
        return option;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images != null ? images : new ArrayList<>();
        for (ProductImage image : this.images) {
            image.setProduct(this);
        }
    }

    public void delete() {
        this.setIsDeleted(true);
        this.options.forEach(ProductOption::delete);
    }

    public void updateBasicInfo(String name, Integer price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

}
