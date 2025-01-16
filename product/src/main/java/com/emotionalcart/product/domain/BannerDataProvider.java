package com.emotionalcart.product.domain;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.ProductBanner;
import com.emotionalcart.core.feature.banner.ProductBannerImage;
import com.emotionalcart.product.infrastructure.repository.BannerRepository;
import com.emotionalcart.product.infrastructure.repository.ProductBannerImageRepository;
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
    private final ProductBannerImageRepository productBannerImageRepository;

    public List<Banner> findAllBanners(){
        LocalDateTime now = LocalDateTime.now();
        return bannerRepository.findByIsDeletedFalseAndStartDateBeforeAndEndDateAfter(now, now);
    }

    public ProductBanner findProductBanner(Long BannerId){
        return productBannerRepository.findByBannerIdAndIsDeletedFalse(BannerId);
    }

    public ProductBannerImage findProductBannerImage(Long productBannerId){
        return productBannerImageRepository.findByProductBannerIdAndIsDeletedFalse(productBannerId);
    }
}
