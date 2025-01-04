package com.emotionalcart.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String imageUrl;

    private String name;

    @Setter
    @Column(length = 4000)
    private String description;

    private int price;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> productOptions;

    public static Product of(String productId, Category category, String imageUrl, String productName, int price) {
        Product product = new Product();
        product.id = Long.parseLong(productId);
        product.category = category;
        product.imageUrl = imageUrl;
        product.name = productName;
        product.description = productName;
        product.price = price;
        category.addProduct(product);
        return product;
    }

    public void addOption(ProductOption option) {
        if (CollectionUtils.isEmpty(productOptions)) {
            productOptions = new ArrayList<>();
        }
        productOptions.add(option);
    }

}
