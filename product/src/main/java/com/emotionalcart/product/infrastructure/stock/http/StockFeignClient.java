package com.emotionalcart.product.infrastructure.stock.http;

import com.emotionalcart.core.config.FeignClientConfig;
import com.emotionalcart.product.infrastructure.stock.dto.StockQuantitySearchRequest;
import com.emotionalcart.product.infrastructure.stock.dto.StockQuantityValidateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "stock-service", url = "${stock.find.feign-endpoint}", path = "/api/v1/stock", configuration = FeignClientConfig.class)
public interface StockFeignClient {

    @PostMapping("/quantity")
    Integer getStockQuantity(@RequestBody StockQuantitySearchRequest request);

    @PostMapping("/quantity/validate")
    ResponseEntity<Boolean> getStockQuantities(@RequestBody StockQuantityValidateRequest requests);
}
