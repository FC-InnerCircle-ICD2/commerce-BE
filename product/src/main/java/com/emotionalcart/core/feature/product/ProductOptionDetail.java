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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    @NotNull
    private String value;

    @NotNull
    private Integer quantity;

    @Size(min = 1)
    @NotNull
    private Integer optionOrder;

    private Integer additionalPrice;

    private ProductOptionDetail(
            String value,
            Integer quantity,
            Integer optionOrder,
            Integer additionalPrice) {
        this.value = value;
        this.quantity = quantity;
        this.optionOrder = optionOrder;
        this.additionalPrice = additionalPrice;
    }
}
