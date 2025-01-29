package com.emotionalcart.core.feature.stock;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.review.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Integer quantity;

    private Stock(
            Long productId,
            Integer quantity
    ) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Stock of(
            Long productId,
            Integer quantity
    ) {
        return new Stock(
                productId,
                quantity
        );
    }
}
