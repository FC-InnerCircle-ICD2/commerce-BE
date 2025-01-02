package com.emotionalcart.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review_statistic")
@Getter
@Setter
@NoArgsConstructor
public class ReviewStatistic extends com.emotionalcart.product.domain.BaseEntity {

    @Id
    private Long productId;

    @Column(name = "avarage_rating", nullable = false)
    private Double averageRating = 0.0;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount = 0;

    public ReviewStatistic(
            Long productId,
            Double averageRating,
            Integer reviewCount
    ) {
        this.productId = productId;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }
}
