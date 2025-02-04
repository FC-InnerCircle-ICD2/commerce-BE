package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.*;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.domain.dto.ProductSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import java.util.*;

@RequiredArgsConstructor
public class QueryDslProductRepositoryImpl implements QueryDslProductRepository {

    private final JPAQueryFactory queryFactory;

    private static final QProduct product = QProduct.product;
    private static final QReviewStatistic reviewStatistic = QReviewStatistic.reviewStatistic;
    private static final QProvider provider = QProvider.provider;
    private static final QCategory category = QCategory.category;
    private static final QProductOption productOption = QProductOption.productOption;

    @Override
    public Page<Product> findAllProducts(ProductSearch productSearch) {

        // 정렬 조건
        OrderSpecifier<?> orderSpecifier = ProductQueryHelper.getOrderSpecifier(productSearch.getSortOption(), product);

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

        List<Product> products  = queryFactory
                .selectDistinct(product)
                .from(product)
                .leftJoin(reviewStatistic).on(product.id.eq(reviewStatistic.productId))
                .where(filterBuilder)
                .offset(productSearch.getPageRequest().getOffset())
                .limit(productSearch.getPageRequest().getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        JPAQuery<Long> count = queryFactory.select(product.count())
                .from(product)
                .where(filterBuilder);

        return PageableExecutionUtils.getPage(products,productSearch.getPageRequest(),count::fetchOne);
    }

    @Override
    public List<ProductOption> findProductOptions(Set<Long> productIds) {
        return queryFactory
                .selectFrom(productOption)
                .where(
                        productOption.product.id.in(productIds), // productIds 조건
                        productOption.isDeleted.eq(false)   // ProductOption 삭제 여부
          
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
  
    @Override
    public List<ProductDetail> findAllProductDetail(Set<Long> productIds) {
        return queryFactory.select(
                        Projections.constructor(
                                ProductDetail.class,
                                product.id,
                                product.price,
                                productOption.id,
                                productOption.isRequired,
                                productOptionDetail.id,
                                productOptionDetail.additionalPrice,
                                productOptionDetail.quantity
                        ))
                .from(product)
                .leftJoin(productOption)
                .on(product.id.eq(productOption.product.id))
                .leftJoin(productOptionDetail)
                .on(productOption.id.eq(productOptionDetail.productOption.id))
                .where(
                        product.id.in(productIds),
                        product.isDeleted.isFalse(),
                        productOption.isDeleted.isFalse(),
                        productOptionDetail.isDeleted.isFalse()
                )
                .fetch();
    }
}
