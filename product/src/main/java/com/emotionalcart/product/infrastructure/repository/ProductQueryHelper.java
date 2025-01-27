package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.QProduct;
import com.emotionalcart.core.feature.product.SortOption;
import com.emotionalcart.core.feature.review.QReviewStatistic;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;

public class ProductQueryHelper {

    public static BooleanBuilder createFilterBuilder(Long productId,
                                                     Long categoryId,
                                                     String keyword,
                                                     Float priceMin,
                                                     Float priceMax,
                                                     Double rating,
                                                     QProduct product,
                                                     QReviewStatistic reviewStatistic) {
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
        addRatingFilter(rating, reviewStatistic, filterBuilder);

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
                    product.category.id.eq(category),
                    product.category.parentCategory.id.eq(category)
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

    private static void addRatingFilter(Double rating, QReviewStatistic reviewStatistic, BooleanBuilder filterBuilder) {
        if (rating != null) {
            filterBuilder.and(reviewStatistic.averageRating.goe(rating));
        }
    }

    public static OrderSpecifier<?> getOrderSpecifier(SortOption sortOption, QProduct product) {
        if (sortOption == null) {
            // order가 null인 경우 기본 정렬 기준으로 처리
            return product.createdAt.desc();
        }
        return switch (sortOption) {
            case CREATE_DESC -> product.createdAt.desc();
            case PRICE_ASC -> product.price.asc();
            case PRICE_DESC -> product.price.desc();
//            case SALES_DESC:
//                return product.salesCount.desc();
            default -> throw new IllegalArgumentException("Invalid SortOption: " + sortOption);
        };
    }
}
