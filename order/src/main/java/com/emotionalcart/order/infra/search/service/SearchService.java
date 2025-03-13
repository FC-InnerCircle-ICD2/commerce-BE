package com.emotionalcart.order.infra.search.service;

import com.emotionalcart.order.infra.search.dto.AdminSearchResponse;
import com.emotionalcart.order.infra.search.http.SearchFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchFeignClient searchFeignClient;

    public AdminSearchResponse search(String keyword) {
        return searchFeignClient.search(keyword).getBody();
    }

}
