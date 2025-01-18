package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.QBanner;
import com.emotionalcart.core.feature.banner.QBannerImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class QueryDslBannerRepositoryImpl implements QueryDslBannerRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Banner> findAllBannersWithImages(LocalDateTime now) {
        QBanner banner = QBanner.banner;
        QBannerImage bannerImage = QBannerImage.bannerImage;

        return queryFactory
                .selectDistinct(banner)
                .from(banner)
                .leftJoin(banner.bannerImage, bannerImage)
                .on(bannerImage.isDeleted.eq(false))
                .where(
                        banner.isDeleted.eq(false),
                        banner.startDate.before(now),
                        banner.endDate.after(now)
                )
                .fetch();
    }
}
