package com.emotionalcart.adminproduct.infrastructure.es.http;

import com.emotionalcart.adminproduct.domain.event.ProductCreatedEvent;
import com.emotionalcart.core.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "elasticsearch-service", url = "${elastic.find.feign-endpoint}", path = "/api/v1/elastic", configuration = FeignClientConfig.class)
public interface ElasticsearchFeignClient {

    @PostMapping("/product")
    ResponseEntity<Void> saveProduct(@RequestBody ProductCreatedEvent event);

}
