package com.emotionalcart.order.infra.stock.http;

import com.emotionalcart.order.infra.config.FeignClientConfig;
import com.emotionalcart.order.infra.stock.dto.DeductedStockInfo;
import com.emotionalcart.order.infra.stock.dto.StockQuantityUpdateRequest;
import com.emotionalcart.order.infra.stock.dto.StockQuantityValidateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "stock-service", url = "${stock.find.feign-endpoint}", path = "/api/v1/stock", configuration = FeignClientConfig.class)
public interface StockFeignClient {

    /**
     * 상품 수량 조회
     */
    @PostMapping("/quantity/validate")
    ResponseEntity<Boolean> validateProductQuantity(@RequestBody StockQuantityValidateRequest request);

    @PostMapping(value = "/deduct", consumes = "application/json")
    ResponseEntity<DeductedStockInfo> deductStockQuantity(@RequestBody StockQuantityUpdateRequest request);

}
