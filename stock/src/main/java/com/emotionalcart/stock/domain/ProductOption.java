package com.emotionalcart.stock.domain;

import com.emotionalcart.stock.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {

    @Id
    @IdGenerator
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(columnDefinition = "varchar(200)")
    private String name;

    @Enumerated(EnumType.STRING)
    private OptionType type;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "is_required")
    private boolean required = false;

    @OneToMany(mappedBy = "productOption", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionDetail> productOptionDetails = new ArrayList<>();

    public static ProductOption of(Product product) {
        ProductOption option = new ProductOption();
        option.product = product;
        product.addOption(option);
        return option;
    }

    public void addDetail(ProductOptionDetail detail) {
        productOptionDetails.add(detail);
    }

    public void defineName(String name) {
        this.name = name;
    }

    public void defineType(OptionType optionType) {
        this.type = optionType;
    }

}
