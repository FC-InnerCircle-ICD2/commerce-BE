package com.emotionalcart.adminproduct.infrastructure.search;

import com.emotionalcart.adminproduct.domain.event.ProductCreatedEvent;
import com.emotionalcart.adminproduct.infrastructure.search.dto.IndexCreateBanner;
import com.emotionalcart.adminproduct.infrastructure.search.dto.IndexCreateProvider;
import com.emotionalcart.adminproduct.infrastructure.search.http.SearchFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchFeignClient searchFeignClient;

    /**
     * 판매처 정보 조회
     */
    public void indexCreateProvider(@RequestBody IndexCreateProvider request) {
        searchFeignClient.indexCreateProvider(request);
    }

    public void indexDeleteProvider(@PathVariable Long providerId) {
        searchFeignClient.indexDeleteProvider(providerId);
    }

    public void indexCreateBanner(@RequestBody IndexCreateBanner request) {
        searchFeignClient.indexCreateBanner(request);
    }

    public void indexDeleteBanner(@PathVariable Long bannerId) {
        searchFeignClient.indexDeleteBanner(bannerId);
    }

    /**
     * 상품 적재
     */
    public void indexCreateProduct(ProductCreatedEvent event) {
        searchFeignClient.indexCreateProduct(event);
    }

}
