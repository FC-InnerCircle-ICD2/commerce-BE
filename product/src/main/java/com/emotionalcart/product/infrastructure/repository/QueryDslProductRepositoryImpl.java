package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.product.presentation.dto.ReadProductStock;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.emotionalcart.core.feature.product.QProduct.product;
import static com.emotionalcart.core.feature.product.QProductOption.productOption;
import static com.emotionalcart.core.feature.product.QProductOptionDetail.productOptionDetail;

@RequiredArgsConstructor
public class QueryDslProductRepositoryImpl implements QueryDslProductRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ReadProductStock.Response> findProductsStock(ReadProductStock.Request request) {
        ReadProductStock.Response response = queryFactory
                .select(Projections.fields(
                        ReadProductStock.Response.class,
                        product.id.as("productId"),
                        productOption.id.as("productOptionId"),
                        productOptionDetail.id.as("productOptionDetailId"),
                        productOption.isRequired,
                        productOptionDetail.quantity
                ))
                .from(product)
                .join(productOption)
                .on(product.id.eq(productOption.productId))
                .join(productOptionDetail)
                .on(productOption.id.eq(productOptionDetail.productOptionId))
                .where(
                        product.id.eq(request.getProductId()),
                        product.isDeleted.isFalse(),
                        productOption.id.eq(request.getProductOptionId()),
                        productOption.isDeleted.isFalse(),
                        productOptionDetail.id.eq(request.getProductOptionDetailId()),
                        productOptionDetail.isDeleted.isFalse()
                )
                .fetchFirst();

        return Optional.ofNullable(response);
    }
}
