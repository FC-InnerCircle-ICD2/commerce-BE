package com.emotionalcart.core.feature.review;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.product.Product;
import jakarta.persistence.*;
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

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

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
