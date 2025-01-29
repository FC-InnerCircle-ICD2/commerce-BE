package com.emotionalcart.product.domain;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.stock.Stock;
import com.emotionalcart.product.infrastructure.StockSearchCondition;
import com.emotionalcart.product.infrastructure.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockDataProvider {
    private final StockRepository stockRepository;

    public Stock findStock(StockSearchCondition condition) {
        return stockRepository.findStockByOptionCombination(condition)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION));
    }

}
