package com.emotionalcart.domain.entity;

import com.emotionalcart.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

    @Id
    @IdGenerator
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ElementCollection
    @CollectionTable(name = "stock_options", joinColumns = @JoinColumn(name = "stock_id"))
    @Column(name = "option_id")
    private List<Long> optionDetailIds;

    private Integer quantity;

    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<StockOption> stockOptions = new ArrayList<>();

    public Stock(Product product, List<Long> optionDetailIds, Integer quantity) {
        this.product = product;
        this.optionDetailIds = optionDetailIds;
        this.quantity = quantity;
        product.addStocks(this);
    }

    public static Stock of(Product product, Integer quantity) {
        Stock stock = new Stock();
        stock.product = product;
        stock.quantity = quantity;
        product.addStocks(stock);
        return stock;
    }

    public void addOption(StockOption stockOption) {
        this.stockOptions.add(stockOption);
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void minusQuantity(int quantity) {
        this.quantity -= quantity;
    }

}