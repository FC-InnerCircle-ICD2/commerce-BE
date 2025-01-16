package com.emotionalcart.core.feature.product;

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
public class ProductOptionDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productOptionId;

    @NotNull
    private String value;

    @NotNull
    private Integer quantity;

    @Size(min = 1)
    @NotNull
    private Integer optionDetailOrder;

    private Integer additionalPrice;

    private ProductOptionDetail(
            Long productOptionId,
            String value,
            Integer quantity,
            Integer optionDetailOrder,
            Integer additionalPrice
    ) {
        this.productOptionId = productOptionId;
        this.value = value;
        this.quantity = quantity;
        this.optionDetailOrder = optionDetailOrder;
        this.additionalPrice = additionalPrice;
    }
}
