package com.emotionalcart.stock.domain;

import com.emotionalcart.generator.IdGenerator;
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

    public void addOption(ProductOption option) {
        productOptions.add(option);
    }

    public void addStocks(Stock stock) {
        stocks.add(stock);
    }

}
