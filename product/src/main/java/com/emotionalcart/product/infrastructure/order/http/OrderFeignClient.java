package com.emotionalcart.product.infrastructure.order.http;

import com.emotionalcart.core.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${order.find.feign-endpoint}", path = "/api/v1/orders", configuration = FeignClientConfig.class)
public interface OrderFeignClient {

    @GetMapping("/users/validate/{orderId}")
    ResponseEntity<Boolean> validateOrder(@PathVariable Long orderId);

}
