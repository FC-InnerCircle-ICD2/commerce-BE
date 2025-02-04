package com.emotionalcart.core.feature.product;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.provider.Provider;

import com.emotionalcart.core.feature.review.ReviewStatistic;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    private ReviewStatistic reviewStatistic;

    private Product(
            Long id,
            String name,
            String description,
            Integer price,
            Long providerId,
            Long categoryId,
            ReviewStatistic reviewStatistic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.providerId = providerId;
        this.categoryId = categoryId;
        this.reviewStatistic = reviewStatistic;
    }
}
