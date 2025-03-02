package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.adminproduct.infrastructure.AdminProducts;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImageType;
import com.emotionalcart.core.feature.product.ProductOption;
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
import static com.emotionalcart.core.feature.product.QProductOption.productOption;
import static com.emotionalcart.core.feature.product.QProductOptionDetail.productOptionDetail;
import static com.emotionalcart.core.feature.provider.QProvider.provider;

@RequiredArgsConstructor
public class AdminQueryDslProductRepositoryImpl implements AdminQueryDslProductRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminProducts> findAllProducts(PageRequest page) {
        JPAQuery<Long> count = queryFactory.select(product.count())
            .from(product)
            .where(product.isDeleted.isFalse());

        List<AdminProducts> content = queryFactory
            .select(Projections.fields(
                AdminProducts.class,
                product.id,
                product.name,
                product.price,
                category.name.as("categoryName"),
                provider.name.as("providerName"),
                productImage.filePath.as("mainImageUrl")
            ))
            .from(product)
            .innerJoin(category)
            .on(category.id.eq(product.categoryId))
            .leftJoin(provider)
            .on(provider.id.eq(product.providerId)) // 현재 provider 구현되어있지 않아 left join 처리
            .innerJoin(productImage)
            .on(productImage.product.eq(product))
            .where(
                product.isDeleted.isFalse(),
                productImage.imageType.eq(ProductImageType.MAIN)
            )
            .offset(page.getOffset())
            .limit(page.getPageSize())
            .fetch();

        return PageableExecutionUtils.getPage(content, page, () -> {
            Long result = count.fetchOne();
            return result != null ? result : 0L;
        });
    }

    @Override
    public void deleteProductOptions(Product product, List<Long> productOptionIds) {
        queryFactory
            .update(productOption)
            .set(productOption.isDeleted, true)
            .where(
                productOption.id.in(productOptionIds),
                productOption.product.eq(product)
            )
            .execute();
    }

    @Override
    public void deleteProductOptionDetails(List<ProductOption> options, List<Long> productOptionDetailIds) {
        queryFactory
            .update(productOptionDetail)
            .set(productOptionDetail.isDeleted, true)
            .where(
                productOptionDetail.id.in(productOptionDetailIds),
                productOptionDetail.productOption.in(options)
            )
            .execute();
    }

}
