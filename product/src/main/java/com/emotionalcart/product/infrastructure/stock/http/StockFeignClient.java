package com.emotionalcart.product.infrastructure.stock.http;

import com.emotionalcart.core.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "stock-service", url = "${stock.find.feign-endpoint}", path = "/api/v1/stock", configuration = FeignClientConfig.class)
public interface StockFeignClient {

    @GetMapping("/quantity")
    Integer getStockQuantity( @RequestParam("productId") Long productId,
                              @RequestParam("optionDetailsIds") List<Long> optionDetailsIds);
}
