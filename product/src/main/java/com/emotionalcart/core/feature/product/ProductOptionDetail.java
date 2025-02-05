package com.emotionalcart.core.feature.product;

import java.util.List;

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
    private String value;

    @NotNull
    private Integer quantity;

    @Size(min = 1)
    @NotNull
    private Integer optionOrder;

    private Integer additionalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    @OneToMany(mappedBy = "productOptionDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImage> images;

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
