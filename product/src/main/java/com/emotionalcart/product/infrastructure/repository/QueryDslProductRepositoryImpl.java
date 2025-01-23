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
    public Page<Product> findAllProducts(ReadProducts.Request request, PageRequest pageRequest) {
        QProduct product = QProduct.product;
        QReviewStatistic reviewStatistic = QReviewStatistic.reviewStatistic;
        QProvider provider = QProvider.provider;
        QCategory category = QCategory.category;

        // 조건 및 정렬 설정
        BooleanBuilder conditions = buildDynamicWhere(request, reviewStatistic);
        OrderSpecifier<?>[] sortOrders = ProductSortConditions.getSortOrders(pageRequest, product);

        List<Product> products  = queryFactory
                .selectDistinct(product)
                .from(product)
                .leftJoin(category).on(product.category.id.eq(category.id)).fetchJoin()
                .leftJoin(provider).on(product.provider.id.eq(provider.id)).fetchJoin()
                .leftJoin(reviewStatistic).on(product.id.eq(reviewStatistic.productId)).fetchJoin()
                .where(conditions)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(sortOrders)
                .fetch();

        return new PageImpl<>(products);
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

    private BooleanBuilder buildDynamicWhere(ReadProducts.Request request, QReviewStatistic reviewStatistic) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(eqProduct(request.getProductId()));
        builder.and(eqCategory(request.getCategoryId()));
        builder.and(eqKeyword(request.getKeyword()));
        builder.and(eqRating(request.getRating(), reviewStatistic));
        builder.and(eqPriceMin(request.getPriceMin()));
        builder.and(eqPriceMax(request.getPriceMax()));

        return builder;
    }

    //상품 검색
    private BooleanExpression eqProduct(Long productId) {
        return productId == null ? null : QProduct.product.id.eq(productId);
    }

    //카테고리 검색
    private BooleanExpression eqCategory(Long categoryId) {
        return categoryId == null ? null : QProduct.product.category.id.eq(categoryId);
    }

    //키워드 검색
    private BooleanExpression eqKeyword(String keyword) {
        return keyword == null ? null : QProduct.product.name.containsIgnoreCase(keyword);
    }

    // 별점 검색
    private BooleanExpression eqRating(Double rating, QReviewStatistic reviewStatistic) {
        return rating == null ? null : reviewStatistic.averageRating.goe(rating);
    }

    // 최소 가격 검색
    private BooleanExpression eqPriceMin(Float priceMin) {
        return priceMin == null ? null : QProduct.product.price.goe(priceMin);
    }

    // 최대 가격 검색
    private BooleanExpression eqPriceMax(Float priceMax) {
        return priceMax == null ? null : QProduct.product.price.loe(priceMax);
    }
}

// 정렬 클래스
class ProductSortConditions {

    public static OrderSpecifier<?>[] getSortOrders(PageRequest pageRequest, QProduct product) {
        return pageRequest.getSort().stream()
                .map(order -> {
                    switch (order.getProperty()) {
                        case "price":
                            return new OrderSpecifier<>(getDirection(order), product.price);
                        case "createdAt":
                            return new OrderSpecifier<>(getDirection(order), product.createdAt);
//                        case "salesCount":
//                            return new OrderSpecifier<>(getDirection(order), product.salesCount);
                        default:
                            throw new IllegalArgumentException("Unknown sorting property: " + order.getProperty());
                    }
                })
                .toArray(OrderSpecifier[]::new);
    }

    private static Order getDirection(Sort.Order order) {
        return order.isAscending() ? Order.ASC : Order.DESC;
    }
}
