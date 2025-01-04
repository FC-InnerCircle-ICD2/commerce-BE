package com.emotionalcart.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewStatistic extends BaseEntity {

    @Id
    private Long productId;

    @NotNull
    private Double averageRating = 0.0;

    @NotNull
    private Integer reviewCount = 0;

    private ReviewStatistic(
            Long productId,
            Double averageRating,
            Integer reviewCount
    ) {
        this.productId = productId;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    public static ReviewStatistic of(
            Long productId,
            Double averageRating,
            Integer reviewCount
    ) {
        return new ReviewStatistic(productId, averageRating, reviewCount);
    }
}
