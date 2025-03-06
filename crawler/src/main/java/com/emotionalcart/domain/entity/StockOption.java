package com.emotionalcart.domain.entity;

import com.emotionalcart.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockOption {

    @Id
    @IdGenerator
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_detail_id", nullable = false)
    private ProductOptionDetail productOptionDetail;

    public static StockOption of(Stock stock, ProductOptionDetail productOptionDetail) {
        StockOption option = new StockOption();
        option.stock = stock;
        option.productOptionDetail = productOptionDetail;
        return option;
    }

}
