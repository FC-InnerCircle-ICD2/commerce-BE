package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.adminproduct.infrastructure.AdminProviders;
import com.emotionalcart.core.feature.product.ProductImageType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.emotionalcart.core.feature.category.QCategory.category;
import static com.emotionalcart.core.feature.product.QProduct.product;
import static com.emotionalcart.core.feature.product.QProductImage.productImage;
import static com.emotionalcart.core.feature.provider.QProvider.provider;

@RequiredArgsConstructor
public class AdminQueryDslProviderRepositoryImpl implements AdminQueryDslProviderRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminProviders> findAllProviders(PageRequest page) {
        JPAQuery<Long> count = queryFactory.select(provider.count())
            .from(provider)
            .where(provider.isDeleted.isFalse());

        List<AdminProviders> content = queryFactory
            .select(Projections.fields(
                AdminProviders.class,
                provider.id,
                provider.name,
                provider.description
            ))
            .from(provider)
            .where(
                provider.isDeleted.isFalse()
            )
            .offset(page.getOffset())
            .limit(page.getPageSize())
            .fetch();

        return PageableExecutionUtils.getPage(content, page, () -> {
            Long result = count.fetchOne();
            return result != null ? result : 0L;
        });
    }

}
