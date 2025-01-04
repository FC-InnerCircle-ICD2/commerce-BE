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
public class ProductBanner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productBannerId;

    @NotNull
    private Long productId;

    private ProductBanner(
            Long productBannerId,
            Long productId
    ) {
        this.productBannerId = productBannerId;
        this.productId = productId;
    }
}
