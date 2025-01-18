package com.emotionalcart.product.domain;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.ProductBanner;
import com.emotionalcart.product.infrastructure.repository.BannerRepository;
import com.emotionalcart.product.infrastructure.repository.ProductBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BannerDataProvider {
    private final BannerRepository bannerRepository;
    private final ProductBannerRepository productBannerRepository;

    public List<Banner> findAllBanners(){
        LocalDateTime now = LocalDateTime.now();
        return bannerRepository.findAllBannersWithImages(now);
    }

    public ProductBanner findProductBanner(Long BannerId){
        return productBannerRepository.findByBannerIdAndIsDeletedFalse(BannerId);
    }
}
