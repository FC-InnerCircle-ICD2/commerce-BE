package com.emotionalcart.product.infrastructure.repository;


import com.emotionalcart.core.feature.stock.Stock;
import com.emotionalcart.product.infrastructure.StockSearchCondition;

import java.util.Optional;

public interface QueryDslStockRepository {

    Optional<Stock> findStockByOptionCombination(StockSearchCondition condition);
}
