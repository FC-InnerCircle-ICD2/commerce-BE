package com.emotionalcart.core.feature.product;

import com.emotionalcart.core.base.BaseEntity;
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

    @NotNull
    private Long productId;

    @NotNull
    private String name;

    @NotNull
    private Boolean isRequired;

    @NotNull
    private String status;

    private ProductOption(
            Long productId,
            String name,
            Boolean isRequired,
            String status
    ) {
        this.productId = productId;
        this.name = name;
        this.isRequired = isRequired;
        this.status = status;
    }
}
