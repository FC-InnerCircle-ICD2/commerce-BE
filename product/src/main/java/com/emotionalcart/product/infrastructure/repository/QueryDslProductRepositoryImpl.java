package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.product.domain.dto.ProductDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import static com.emotionalcart.core.feature.product.QProduct.product;
import static com.emotionalcart.core.feature.product.QProductOption.productOption;
import static com.emotionalcart.core.feature.product.QProductOptionDetail.productOptionDetail;

@RequiredArgsConstructor
public class QueryDslProductRepositoryImpl implements QueryDslProductRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductDetail> findAllProductData(Set<Long> productIds) {
        return queryFactory.select(
                        Projections.constructor(
                                ProductDetail.class,
                                product.id,
                                productOption.id,
                                productOption.isRequired,
                                productOptionDetail.id,
                                productOptionDetail.quantity
                        ))
                .from(product)
                .leftJoin(productOption)
                .on(product.id.eq(productOption.productId))
                .leftJoin(productOptionDetail)
                .on(productOption.id.eq(productOptionDetail.productOptionId))
                .where(
                        product.id.in(productIds),
                        product.isDeleted.isFalse(),
                        productOption.isDeleted.isFalse(),
                        productOptionDetail.isDeleted.isFalse()
                )
                .fetch();
    }

}
