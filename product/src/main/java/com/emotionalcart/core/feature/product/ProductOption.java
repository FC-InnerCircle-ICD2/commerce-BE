package com.emotionalcart.core.feature.product;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.category.Category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private String name;

    @NotNull
    private Boolean isRequired;

    private ProductOption(
            String name,
            Boolean isRequired) {
        this.name = name;
        this.isRequired = isRequired;
    }
}
