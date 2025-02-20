package com.emotionalcart.core.feature.review;

import com.emotionalcart.core.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @CreatedBy
    private Long userId;

    @NotNull
    private Long productId;

    private String productName;

    private String productOptionId;

    private String productOptionName;

    @NotNull
    private Integer rating;

    private String content;

    private Review(
        Long productId,
        String productName,
        String productOptionId,
        String productOptionName,
        Integer rating,
        String content
    ) {
        this.productId = productId;
        this.productName = productName;
        this.productOptionId = productOptionId;
        this.productOptionName = productOptionName;
        this.rating = rating;
        this.content = content;
    }

    public static Review of(
        Long productId,
        String productName,
        String productOptionId,
        String productOptionName,
        Integer rating,
        String content
    ) {
        return new Review(
            productId,
            productName,
            productOptionId,
            productOptionName,
            rating,
            content
        );
    }

}