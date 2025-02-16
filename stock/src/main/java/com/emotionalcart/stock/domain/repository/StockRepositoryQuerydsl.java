package com.emotionalcart.stock.domain.repository;

import com.emotionalcart.stock.domain.Stock;
import com.emotionalcart.stock.domain.StockQuantitySearchCondition;

import java.util.Optional;

public interface StockRepositoryQuerydsl {

    /**
     * 재고 수량 조회
     *
     * @param searchCondition 재고 수량 조회 조건
     * @return 재고 수량
     */
    int findStockQuantityByOptionIds(StockQuantitySearchCondition searchCondition);

    /**
     * 재고 조회
     *
     * @param searchCondition 재고 조회 조건
     * @return 재고
     */
    Optional<Stock> getStockByOptionIds(StockQuantitySearchCondition searchCondition);

}
