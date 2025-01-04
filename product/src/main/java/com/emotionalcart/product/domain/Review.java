package com.emotionalcart.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String productId;

    private String productName;

    private String productOptionId;

    private String productOptionName;

    @Size(min = 1, max = 5)
    @NotNull
    private String rating;

    private String content;

    public Review(
            String userId,
            String productId,
            String productName,
            String productOptionId,
            String productOptionName,
            String rating,
            String content
    ) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productOptionId = productOptionId;
        this.productOptionName = productOptionName;
        this.rating = rating;
        this.content = content;
    }
}