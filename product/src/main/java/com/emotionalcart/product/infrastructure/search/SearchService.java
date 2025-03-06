package com.emotionalcart.product.infrastructure.search;

import com.emotionalcart.product.infrastructure.search.dto.IndexCreateProvider;
import com.emotionalcart.product.infrastructure.search.http.SearchFeignClient;
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
}
