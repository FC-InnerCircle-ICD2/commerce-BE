package com.emotionalcart.adminproduct.domain;

import com.emotionalcart.adminproduct.infrastructure.repository.AdminBannerImageRepository;
import com.emotionalcart.adminproduct.infrastructure.repository.AdminBannerRepository;
import com.emotionalcart.adminproduct.infrastructure.repository.AdminProductBannerRepository;
import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.BannerImage;
import com.emotionalcart.core.feature.banner.ProductBanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBannerDataProvider {
    private final AdminBannerRepository adminBannerRepository;
    private final AdminBannerImageRepository adminBannerImageRepository;
    private final AdminProductBannerRepository adminProductBannerRepository;

    public Banner saveBanner(Banner banner) {
        return adminBannerRepository.save(banner);
    }

    public void saveBannerImage(BannerImage bannerImage){
        adminBannerImageRepository.save(bannerImage);
    }

    public void saveProductBanner(ProductBanner productBanner){
        adminProductBannerRepository.save(productBanner);
    }
}
