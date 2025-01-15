package com.emotionalcart.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentCategoryId;

    @NotNull
    private String name;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Integer depth;

    private Category(
            Long parentCategoryId,
            String name,
            Boolean isActive,
            Integer depth
    ) {
        this.parentCategoryId = parentCategoryId;
        this.name = name;
        this.isActive = isActive;
        this.depth = depth;
    }
}
