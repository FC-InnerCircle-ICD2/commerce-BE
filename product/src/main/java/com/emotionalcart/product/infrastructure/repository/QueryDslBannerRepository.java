package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.banner.Banner;

import java.time.LocalDateTime;
import java.util.List;

public interface QueryDslBannerRepository {
    List<Banner> findAllBannersWithImages(LocalDateTime now);
}
