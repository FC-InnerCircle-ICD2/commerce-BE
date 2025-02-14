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

    private Integer totalRating = 0;

    @NotNull
    private Double averageRating = 0.0;

    @NotNull
    private Integer reviewCount = 0;

    private ReviewStatistic(
            Long productId,
            Integer totalRating,
            Double averageRating,
            Integer reviewCount
    ) {
        this.productId = productId;
        this.totalRating = totalRating;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    public static ReviewStatistic of(
            Long productId,
            Integer totalRating,
            Double averageRating,
            Integer reviewCount
    ) {
        return new ReviewStatistic(productId, totalRating, averageRating, reviewCount);
    }

    /**
     * 리뷰 통계 데이터 업데이트
     */
    public void updateStatistics(Integer newRating) {
        this.totalRating += newRating;
        this.reviewCount++;
        this.averageRating = (double) this.totalRating / this.reviewCount;
    }
}
