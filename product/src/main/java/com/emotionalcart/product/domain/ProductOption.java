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
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private String name;

    @NotNull
    private Integer quantity;

    @Size(min = 1)
    @NotNull
    private Integer optionOrder;

    @NotNull
    private Boolean isRequired;

    @NotNull
    private Integer sellPrice;

    @NotNull
    private String status;

    private ProductOption(
            Long productId,
            String name,
            Integer quantity,
            Integer optionOrder,
            Boolean isRequired,
            Integer sellPrice,
            String status
    ) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.optionOrder = optionOrder;
        this.isRequired = isRequired;
        this.sellPrice = sellPrice;
        this.status = status;
    }
}
