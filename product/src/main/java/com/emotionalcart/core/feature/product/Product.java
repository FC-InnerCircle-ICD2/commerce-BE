package com.emotionalcart.core.feature.product;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.provider.Provider;

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
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", insertable = false, updatable = false)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    private Product(
            Long id,
            String name,
            String description,
            Integer price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
