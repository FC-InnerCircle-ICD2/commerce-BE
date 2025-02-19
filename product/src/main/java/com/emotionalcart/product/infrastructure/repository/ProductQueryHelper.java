package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.order.QOrderStatistics;
import com.emotionalcart.core.feature.product.QProduct;
import com.emotionalcart.core.feature.product.SortOption;
import com.emotionalcart.core.feature.review.QReviewStatistic;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;

import java.util.List;

import static com.emotionalcart.core.feature.review.QReviewStatistic.reviewStatistic;

public class ProductQueryHelper {

    public static BooleanBuilder createFilterBuilder(Long productId,
                                                     Long categoryId,
                                                     String keyword,
                                                     Float priceMin,
                                                     Float priceMax,
                                                     Double rating,
                                                     QProduct product) {
        BooleanBuilder filterBuilder = new BooleanBuilder();

        // 상품 ID 필터링
        addProductFilter(productId, product, filterBuilder);

        // 카테고리 필터링
        addCategoryFilter(categoryId, product, filterBuilder);
        // 검색
        addKeywordFilter(keyword, product, filterBuilder);

        // 가격 범위 필터링
        addPriceFilter(priceMin, priceMax, product, filterBuilder);

        // 별점 필터링
        addRatingFilter(rating, filterBuilder);

        return filterBuilder;
    }

    private static void addProductFilter(Long productId, QProduct product, BooleanBuilder filterBuilder) {
        if (productId != null) {
            filterBuilder.and(product.id.eq(productId));
        }
    }

    private static void addCategoryFilter(Long category, QProduct product, BooleanBuilder filterBuilder) {
        if (category != null) {
            filterBuilder.andAnyOf(
                    product.categoryId.eq(category)
                    //product.category.parentCategory.id.eq(category)
            );
        }
    }

    private static void addKeywordFilter(String keyword, QProduct product, BooleanBuilder filterBuilder) {
        if (keyword != null) {
            filterBuilder.and(
                    product.name.containsIgnoreCase(keyword)
                            .or(product.description.containsIgnoreCase(keyword))
            );
        }
    }

    private static void addPriceFilter(Float priceMin, Float priceMax, QProduct product, BooleanBuilder filterBuilder) {
        if (priceMin != null) {
            filterBuilder.and(product.price.goe(priceMin));
        }
        if (priceMax != null) {
            filterBuilder.and(product.price.loe(priceMax));
        }
    }

    private static void addRatingFilter(Double rating, BooleanBuilder filterBuilder) {
        if (rating != null) {
            filterBuilder.and(reviewStatistic.averageRating.goe(rating));
        }
    }

    public static OrderSpecifier<?>[] getOrderSpecifier(SortOption sortOption, QProduct product, QOrderStatistics orderStatistics) {
        if (sortOption == null) {
            // order가 null인 경우 기본 정렬 기준으로 처리
            return new OrderSpecifier[]{product.createdAt.desc()};
        }
        return switch (sortOption) {
            case CREATE_DESC -> new OrderSpecifier[]{product.createdAt.desc()};
            case PRICE_ASC -> new OrderSpecifier[]{product.price.asc()};
            case PRICE_DESC -> new OrderSpecifier[]{product.price.desc()};
            case SALES_DESC -> new OrderSpecifier[]{orderStatistics.totalQuantitySold.desc(), product.createdAt.desc()};
            default -> throw new IllegalArgumentException("Invalid SortOption: " + sortOption);
        };
    }

//    private static OrderSpecifier<?> getSalesOrderSpecifier(QProduct product) {
//        // 1️⃣ 판매량 기준 정렬 (높은 순)
//        NumberExpression<Integer> salesOrder = new CaseBuilder()
//                .when(product.id.in(sortedProductIds))
//                .then(
//                        new CaseBuilder()
//                                .when(product.id.eq(sortedProductIds.get(0))).then(0)
//                                .when(product.id.eq(sortedProductIds.get(1))).then(1)
//                                .when(product.id.eq(sortedProductIds.get(2))).then(2)
//                                .otherwise(sortedProductIds.size()) // 리스트에 없는 경우 동일한 값 적용
//                )
//                .otherwise(sortedProductIds.size()); // 판매량 없는 상품들
//        return salesOrder.asc();
//    }
}
