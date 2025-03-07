package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.*;
import com.emotionalcart.core.feature.provider.*;
import com.emotionalcart.product.domain.dto.ProductSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import com.emotionalcart.product.domain.dto.ProductDetail;

import java.util.List;
import java.util.Set;

import static com.emotionalcart.core.feature.product.QProduct.product;
import static com.emotionalcart.core.feature.product.QProductOption.productOption;
import static com.emotionalcart.core.feature.product.QProductOptionDetail.productOptionDetail;
import static com.emotionalcart.core.feature.review.QReviewStatistic.reviewStatistic;
import static com.emotionalcart.core.feature.order.QOrderStatistics.orderStatistics;

@RequiredArgsConstructor
public class QueryDslProductRepositoryImpl implements QueryDslProductRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findAllProducts(ProductSearch productSearch) {

        // 정렬 조건
        OrderSpecifier<?>[] orderSpecifier = ProductQueryHelper.getOrderSpecifier(productSearch.getSortOption(), product, orderStatistics);

        // 필터 조건 생성
        BooleanBuilder filterBuilder = ProductQueryHelper.createFilterBuilder(
            productSearch.getProductId(),
            productSearch.getCategoryId(),
            productSearch.getKeyword(),
            productSearch.getPriceMin(),
            productSearch.getPriceMax(),
            productSearch.getRating(),
            product
        );

        List<Product> products = queryFactory
            .selectDistinct(product)
            .from(product)
            .leftJoin(orderStatistics).on(orderStatistics.productId.eq(product.id)) // 명시적 조인
            .leftJoin(product.reviewStatistic, reviewStatistic).fetchJoin()
            .where(
                filterBuilder,
                product.isDeleted.isFalse()
            )
            .offset(productSearch.getPageRequest().getOffset())
            .limit(productSearch.getPageRequest().getPageSize())
            .orderBy(orderSpecifier)
            .fetch();

        JPAQuery<Long> count = queryFactory.select(product.count())
            .from(product)
            .where(
                filterBuilder,
                product.isDeleted.isFalse()
            );

        return PageableExecutionUtils.getPage(products, productSearch.getPageRequest(), count::fetchOne);
    }

    @Override
    public List<ProductOption> findProductOptions(List<Long> productIds) {
        return queryFactory
            .selectDistinct(productOption)
            .from(productOption)
            .leftJoin(productOption.details, productOptionDetail).fetchJoin()
            .where(
                productOption.product.id.in(productIds), // productIds 조건
                productOption.isDeleted.isFalse(),
                productOptionDetail.isDeleted.isFalse()) // ProductOption 삭제 여부
            .fetch();
    }

    @Override
    public List<ProductDetail> findAllProductDetail(Set<Long> productIds) {
        QProvider provider = QProvider.provider;
        return queryFactory.select(
                Projections.constructor(
                    ProductDetail.class,
                    product.id,
                    product.providerId,
                    provider.name,
                    product.price,
                    productOption.id,
                    productOption.name,
                    productOptionDetail.id,
                    productOptionDetail.value,
                    productOptionDetail.additionalPrice
                ))
            .from(product)
            .leftJoin(productOption)
            .on(product.id.eq(productOption.product.id))
            .leftJoin(productOptionDetail)
            .on(productOption.id.eq(productOptionDetail.productOption.id))
            .join(provider)
            .on(product.providerId.eq(provider.id))
            .where(
                product.id.in(productIds),
                product.isDeleted.isFalse(),
                productOption.isDeleted.isFalse(),
                productOptionDetail.isDeleted.isFalse(),
                provider.isDeleted.isFalse()
            )
            .fetch();
    }

}
