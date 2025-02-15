package com.emotionalcart.core.feature.product;

import com.emotionalcart.core.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductOptionDetail> details;

    private ProductOption(String name) {
        this.name = name;
    }

    public static ProductOption of(String name) {
        return new ProductOption(name);
    }

    public void setDetails(List<ProductOptionDetail> details) {
        this.details = details != null ? details : new ArrayList<>();
        for (ProductOptionDetail detail : this.details) {
            detail.setProductOption(this);
        }
    }


    public Long getProductId() {
        return product.getId();
    }
}
