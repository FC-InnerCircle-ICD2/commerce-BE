package com.emotionalcart.core.feature.product;

import com.emotionalcart.core.base.BaseEntity;
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
    private Long categoryId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer price;

    private Product(
            Long id,
            Long categoryId,
            String name,
            String description,
            Integer price
    ) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}