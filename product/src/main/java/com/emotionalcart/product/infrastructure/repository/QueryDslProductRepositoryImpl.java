package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.category.QCategory;
import com.emotionalcart.core.feature.product.*;
import com.emotionalcart.core.feature.review.QReviewStatistic;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.presentation.dto.ReadProducts;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.*;

@RequiredArgsConstructor
public class QueryDslProductRepositoryImpl implements QueryDslProductRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findAllProducts(Long productId, Long categoryId, String keyword,
                                         Float priceMin, Float priceMax, Double rating,
                                         SortOption sortOption, PageRequest pageRequest) {
        QProduct product = QProduct.product;
        QReviewStatistic reviewStatistic = QReviewStatistic.reviewStatistic;
        QProvider provider = QProvider.provider;
        QCategory category = QCategory.category;

        // 정렬 조건
        OrderSpecifier<?> orderSpecifier = ProductQueryHelper.getOrderSpecifier(sortOption, product);

        // 필터 조건 생성
        BooleanBuilder filterBuilder = ProductQueryHelper.createFilterBuilder(
                productId, categoryId, keyword, priceMin, priceMax, rating, product, reviewStatistic
        );

        List<Product> products  = queryFactory
                .selectFrom(product)
                .leftJoin(category).on(product.category.id.eq(category.id)).fetchJoin()
                .leftJoin(provider).on(product.provider.id.eq(provider.id)).fetchJoin()
                .leftJoin(reviewStatistic).on(product.id.eq(reviewStatistic.productId)).fetchJoin()
                .where(filterBuilder)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        long totalCount = queryFactory
                .selectFrom(product)
                .where(filterBuilder)
                .fetchCount();

        return new PageImpl<>(products, pageRequest, totalCount);
    }

    @Override
    public List<ProductOption> findProductOptions(Set<Long> productIds) {
        QProductOption productOption = QProductOption.productOption;

        return queryFactory
                .selectFrom(productOption)
                .where(
                        productOption.product.id.in(productIds), // productIds 조건
                        productOption.isDeleted.eq(false)   // ProductOption 삭제 여부
                )
                .fetch();
    }

    @Override
    public List<ProductOptionDetailWithImages> findProductOptionDetailsWithImages(Set<Long> optionIds) {
        QProductOptionDetail productOptionDetail = QProductOptionDetail.productOptionDetail;
        QProductImage productImage = QProductImage.productImage;

        return queryFactory
                .select(Projections.constructor(
                        ProductOptionDetailWithImages.class, // DTO 클래스
                        productOptionDetail.id,
                        productOptionDetail.productOption.id,
                        productOptionDetail.value,
                        productOptionDetail.quantity,
                        productOptionDetail.additionalPrice,
                        productImage.id,
                        productImage.fileOrder,
                        productImage.filePath
                ))
                .from(productOptionDetail)
                .leftJoin(productImage).on(productOptionDetail.id.eq(productImage.productOptionDetail.id)) // 이미지와 조인
                .where(
                        productOptionDetail.productOption.id.in(optionIds),
                        productOptionDetail.isDeleted.eq(false),
                        productImage.isDeleted.isNull().or(productImage.isDeleted.eq(false)),
                        productImage.isRepresentative.isNull().or(productImage.isRepresentative.eq(true))
                )
                .fetch();
    }
}
