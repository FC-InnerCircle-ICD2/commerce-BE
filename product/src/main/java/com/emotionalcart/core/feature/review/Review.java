package com.emotionalcart.core.feature.review;

import com.emotionalcart.core.base.BaseEntity;
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
    private Long productId;

    private String productName;

    private String productOptionId;

    private String productOptionName;

    @NotNull
    private Integer rating;

    private String content;

    private Review(
            String userId,
            Long productId,
            String productName,
            String productOptionId,
            String productOptionName,
            Integer rating,
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

    public static Review of(
            Long productId,
            String productName,
            String productOptionId,
            String productOptionName,
            Integer rating,
            String content
    ) {
        return new Review(
                "userId", // TODO 회원가입 적용 이후 반영
                productId,
                productName,
                productOptionId,
                productOptionName,
                rating,
                content
        );
    }
}