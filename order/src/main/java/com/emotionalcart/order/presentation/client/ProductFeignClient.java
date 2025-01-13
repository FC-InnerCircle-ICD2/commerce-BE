package com.emotionalcart.order.presentation.client;

import com.emotionalcart.order.infrastructure.configuration.FeignConfig;
import com.emotionalcart.order.presentation.client.dto.ProductStockRequest;
import com.emotionalcart.order.presentation.client.dto.UpdateStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 상품 호출 API
 *
 * @author yeji cho
 * @since 2024.1.12
 **/
@FeignClient(name = "product-service", url = "${product.find.feign-endpoint}", path = "/api/v1/product", configuration = FeignConfig.class)
public interface ProductFeignClient {

    /**
     * 상품 재고 조회
     *
     * @return
     */
    @GetMapping("/stock")
    <T> ResponseEntity<T> getProductStock(@SpringQueryMap List<ProductStockRequest> request);

    /**
     * 상품 재고 변경
     *
     * @return
     */
    @PutMapping("/stock")
    <T> ResponseEntity<T> updateProductStock(@RequestBody UpdateStockRequest request);

}
