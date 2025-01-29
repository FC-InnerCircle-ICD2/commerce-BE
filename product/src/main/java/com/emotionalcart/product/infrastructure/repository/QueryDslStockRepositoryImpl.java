package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.stock.Stock;
import com.emotionalcart.product.infrastructure.StockSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import static com.emotionalcart.core.feature.stock.QStock.stock;
import static com.emotionalcart.core.feature.stock.QStockOption.stockOption;

@RequiredArgsConstructor
public class QueryDslStockRepositoryImpl implements QueryDslStockRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * 특정 상품과 옵션 조합을 만족하는 Stock 조회
     */
    @Override
    public Optional<Stock> findStockByOptionCombination(StockSearchCondition condition) {
        Stock result = queryFactory
                .select(stock)
                .from(stock)
                .where(
                        stock.productId.eq(condition.getProductId()),
                        stock.id.in(
                                queryFactory
                                        .select(stockOption.stockId)
                                        .from(stockOption)
                                        .where(buildOptionCondition(condition.getOptionMap()))
                                        .groupBy(stockOption.stockId)
                                        .having(stockOption.stockId.count().eq((long) condition.getOptionMap().size()))

                        )
                )
                .fetchFirst();
        return Optional.ofNullable(result);
    }

    /**
     * 옵션 조건을 동적으로 생성하는 메서드
     */
    private BooleanBuilder buildOptionCondition(Map<Long, Long> optionMap) {
        BooleanBuilder builder = new BooleanBuilder();

        if (optionMap.isEmpty()) {
            return builder;
        }

        BooleanBuilder orConditions = new BooleanBuilder();
        optionMap.forEach((optionId, detailId) ->
                orConditions.or(
                        stockOption.productOptionId.eq(optionId)
                                .and(stockOption.productOptionDetailId.eq(detailId))
                )
        );

        builder.and(orConditions);
        return builder;
    }
}
