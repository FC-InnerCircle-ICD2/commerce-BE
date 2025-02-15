package com.emotionalcart.stock.domain.repository;

import com.emotionalcart.stock.domain.Querydsl5RepositorySupport;
import com.emotionalcart.stock.domain.Stock;
import com.emotionalcart.stock.domain.StockQuantitySearchCondition;

import java.util.Optional;

import static com.emotionalcart.stock.domain.QStock.stock;
import static com.emotionalcart.stock.domain.QStockOption.stockOption;

public class StockRepositoryImpl extends Querydsl5RepositorySupport implements StockRepositoryQuerydsl {

    protected StockRepositoryImpl() {
        super(Stock.class);
    }

    @Override
    public int findStockQuantityByOptionIds(StockQuantitySearchCondition searchCondition) {
        Integer stockCount = select(stock.quantity)
            .from(stock)
            .join(stockOption).on(stock.id.eq(stockOption.stock.id))
            .where(
                stock.product.id.eq(searchCondition.getProductId()),
                stockOption.productOptionDetail.id.in(searchCondition.getOptionDetailsIds()))
            .groupBy(stock.id)
            .having(stockOption.productOptionDetail.id.countDistinct().eq((long)searchCondition.getOptionDetailsIds().size()))
            .fetchOne();
        return stockCount != null ? stockCount : 0;
    }

    @Override
    public Optional<Stock> getStockByOptionIds(StockQuantitySearchCondition searchCondition) {
        return Optional.ofNullable(
            selectFrom(stock)
                .join(stockOption).on(stock.id.eq(stockOption.stock.id))
                .where(
                    stock.product.id.eq(searchCondition.getProductId()),
                    stockOption.productOptionDetail.id.in(searchCondition.getOptionDetailsIds()))
                .groupBy(stock.id)
                .having(stockOption.productOptionDetail.id.countDistinct().eq((long)searchCondition.getOptionDetailsIds().size()))
                .fetchOne()
        );
    }

}
