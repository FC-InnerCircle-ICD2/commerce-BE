package com.emotionalcart.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
public class Review extends com.emotionalcart.product.domain.BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_option_id")
    private String productOptionId;

    @Column(name = "product_option_name")
    private String productOptionName;

    @Size(min = 1, max = 5)
    @Column(name = "rating", nullable = false)
    private String rating;

    @Column(name = "content")
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