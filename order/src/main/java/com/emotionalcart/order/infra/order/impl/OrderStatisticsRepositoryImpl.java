package com.emotionalcart.order.infra.order.impl;

import com.emotionalcart.order.domain.dto.BestSellingProduct;
import com.emotionalcart.order.domain.entity.QOrderStatistics;
import com.emotionalcart.order.infra.order.OrderStatisticsQuerydsl;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class OrderStatisticsRepositoryImpl implements OrderStatisticsQuerydsl {

    private final JPQLQueryFactory queryFactory;

    @Override
    public Page<BestSellingProduct> getProductRankingsByCategoryId(Long categoryId, Pageable page) {
        QOrderStatistics orderStatistics = QOrderStatistics.orderStatistics;
        List<BestSellingProduct> content = queryFactory.select(Projections.constructor(
                BestSellingProduct.class,
                orderStatistics.productId,
                orderStatistics.categoryId,
                orderStatistics.totalOrder,
                orderStatistics.totalQuantitySold
            )).from(orderStatistics)
            .where(orderStatistics.categoryId.eq(categoryId))
            .orderBy(orderStatistics.totalQuantitySold.desc())
            .offset(page.getOffset())
            .limit(page.getPageSize()).fetch();
        long count = queryFactory.select(orderStatistics.count()).from(orderStatistics)
            .where(orderStatistics.categoryId.eq(categoryId)).fetchCount();
        return new PageImpl<>(content, page, count);
    }

}
