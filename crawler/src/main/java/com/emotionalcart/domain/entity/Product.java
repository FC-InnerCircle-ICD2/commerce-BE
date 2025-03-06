package com.emotionalcart.domain.entity;

import com.emotionalcart.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @IdGenerator
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
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
    private List<ProductOption> productOptions = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewStatistic> reviewStatistics = new ArrayList<>();

    public static Product of(Category category, String imageUrl, String productName, int price) {
        Product product = new Product();
        product.category = category;
        product.imageUrl = imageUrl;
        product.name = productName;
        product.description = productName;
        product.price = price;
        category.addProduct(product);
        return product;
    }

    public void addOption(ProductOption option) {
        productOptions.add(option);
    }

    public void addStocks(Stock stock) {
        stocks.add(stock);
    }

    public void addImages(ProductImage image) {
        image.markOrder(images.size());
        images.add(image);
    }

    public void addReviewStatistics(ReviewStatistic reviewStatistic) {
        reviewStatistics.add(reviewStatistic);
    }

}
