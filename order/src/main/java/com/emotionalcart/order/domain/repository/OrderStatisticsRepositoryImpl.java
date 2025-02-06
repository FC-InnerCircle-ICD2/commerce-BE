package com.emotionalcart.order.domain.repository;

import com.emotionalcart.order.infra.dto.BestSellingProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.emotionalcart.order.domain.entity.QOrderStatistics.orderStatistics;
import static com.querydsl.core.types.Projections.constructor;

@RequiredArgsConstructor
public class OrderStatisticsRepositoryImpl implements OrderStatisticsQuerydsl {

    private final JPQLQueryFactory queryFactory;

    public BooleanExpression hasCategoryId(Long categoryId) {
        return categoryId != null ? orderStatistics.categoryId.eq(categoryId) : null;
    }

    public BooleanExpression hasProductId(Long productId) {
        return productId != null ? orderStatistics.productId.eq(productId) : null;
    }

    @Override
    public Page<BestSellingProduct> getProductRankingsByCategoryId(Long categoryId, Pageable page) {

        // 공통 조건을 static 메서드로 분리하여 사용
        BooleanExpression categoryCondition = hasCategoryId(categoryId);

        // 데이터 조회 쿼리
        List<BestSellingProduct> content = queryFactory
            .select(constructor(
                BestSellingProduct.class,
                orderStatistics.productId,
                orderStatistics.categoryId,
                orderStatistics.totalOrder,
                orderStatistics.totalQuantitySold
            ))
            .from(orderStatistics)
            .where(categoryCondition)
            .orderBy(orderStatistics.totalQuantitySold.desc())
            .offset(page.getOffset())
            .limit(page.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = (JPAQuery<Long>)queryFactory
            .select(orderStatistics.count())
            .from(orderStatistics)
            .where(categoryCondition);

        return PageableExecutionUtils.getPage(content, page, countQuery::fetchOne);
    }

}
