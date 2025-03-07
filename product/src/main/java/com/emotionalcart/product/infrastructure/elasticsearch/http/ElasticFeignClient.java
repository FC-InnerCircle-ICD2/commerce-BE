package com.emotionalcart.product.infrastructure.elasticsearch.http;

import com.emotionalcart.core.config.FeignClientConfig;
import com.emotionalcart.product.infrastructure.elasticsearch.dto.ElasticProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "elastic-service", url = "${search.find.feign-endpoint}", path = "/api/v1/elastic", configuration = FeignClientConfig.class)
public interface ElasticFeignClient {

    @GetMapping("/product")
    ResponseEntity<List<ElasticProduct>> searchProducts(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "sortOption", required = false) String sortOption,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "minPrice", required = false) Integer minPrice,
        @RequestParam(value = "maxPrice", required = false) Integer maxPrice
    );
}
