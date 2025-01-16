package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.ProductBanner;
import com.emotionalcart.core.feature.banner.ProductBannerImage;
import com.emotionalcart.product.domain.BannerDataProvider;
import com.emotionalcart.product.presentation.dto.ReadBanners;
import com.emotionalcart.product.presentation.dto.ReadProductBanners;

import java.util.List;

public class Banners {

    private final List<ReadBanners.Response> banners;

    private Banners(List<ReadBanners.Response> banners){
        this.banners = banners;
    }

    public static Banners from(List<Banner> banners, BannerDataProvider bannerDataProvider){
        List<ReadBanners.Response> responseList = banners.stream()
                .map(banner -> mapBannerToResponse(banner, bannerDataProvider))
                .toList();
        return new Banners(responseList);
    }

    public List<ReadBanners.Response> getBanners() {
        return banners;
    }

    private static ReadBanners.Response mapBannerToResponse(Banner banner, BannerDataProvider bannerDataProvider) {
        return switch (banner.getType()) {
            case PRODUCT -> fromProductBanner(banner, bannerDataProvider); // 상품 배너 처리
            //case EVENT -> fromEventBanner(banner, bannerDataProvider);   // 이벤트 배너 처리
            default -> new ReadBanners.Response(banner);      // 기본 배너 처리
        };
    }

    private static ReadBanners.Response fromProductBanner(Banner banner, BannerDataProvider bannerDataProvider) {
        ProductBanner productBanner = bannerDataProvider.findProductBanner(banner.getId());
        if (productBanner == null) {
            return new ReadBanners.Response(banner);
        }

        ProductBannerImage image = bannerDataProvider.findProductBannerImage(productBanner.getId());
        ReadProductBanners.Response productResponse = ReadProductBanners.Response.toResponse(productBanner, image);

        return new ReadBanners.Response(banner, productResponse);
    }
}
