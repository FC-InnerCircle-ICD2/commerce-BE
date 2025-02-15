package com.emotionalcart.stock.application;

import com.emotionalcart.stock.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockQueryService {

    private final StockRepository stockRepository;

    /**
     * <pre>
     * 재고 조회
     * 옵션 디테일 식별자 목록으로 재고 수량을 조회한다.
     * </pre>
     *
     * @param query 재고 조회 조건
     */
    public int getStockQuantityByOptionIds(StockQuantitySearchQuery query) {
        return stockRepository.findStockQuantityByOptionIds(query.mapToCondition());
    }

}