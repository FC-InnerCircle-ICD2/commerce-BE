package com.emotionalcart.order.infra.search.http;

import com.emotionalcart.order.infra.config.FeignClientConfig;
import com.emotionalcart.order.infra.search.dto.AdminSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "search-service", url = "${search.find.feign-endpoint}", path = "/api/v1/admin/search", configuration = FeignClientConfig.class)
public interface SearchFeignClient {

    @GetMapping
    ResponseEntity<AdminSearchResponse> search(@RequestParam("keyword") String keyword);

}
