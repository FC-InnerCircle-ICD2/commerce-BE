package com.emotionalcart.order.infra.search.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AdminSearchResponse {

    private List<ProductAdminResponse> products;

    private List<OrderSearchResponse> orders;
    private List<BannerSearchResponse> banners;
    List<ProviderSearchResponse> providers;

    public static AdminSearchResponse of(List<ProductAdminResponse> products, List<OrderSearchResponse> orders,
                                         List<BannerSearchResponse> banners, List<ProviderSearchResponse> providers) {
        AdminSearchResponse response = new AdminSearchResponse();
        response.products = products;
        response.orders = orders;
        response.banners = banners;
        response.providers = providers;
        return response;
    }

}
