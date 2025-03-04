package com.emotionalcart.core.feature.product;

import com.emotionalcart.core.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String value;

    @NotNull
    @Column(nullable = false)
    private Integer quantity = 0;

    @NotNull
    private Integer optionOrder = 1;

    private Integer additionalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    @Setter
    private ProductOption productOption;

    private ProductOptionDetail(
        String value,
        Integer optionOrder,
        Integer additionalPrice,
        ProductOption productOption
    ) {
        this.value = value;
        this.optionOrder = optionOrder == null ? 1 : optionOrder;
        this.additionalPrice = additionalPrice;
        this.productOption = productOption;
        this.quantity = 0;
    }

    public static ProductOptionDetail of(String value, Integer optionOrder, Integer additionalPrice, ProductOption productOption) {
        return new ProductOptionDetail(
            value,
            optionOrder,
            additionalPrice,
            productOption
        );
    }

    public void delete() {
        this.setIsDeleted(true);
    }

    public void updateValue(String value, Integer additionalPrice) {
        this.value = value;
        this.additionalPrice = additionalPrice;
    }
}
