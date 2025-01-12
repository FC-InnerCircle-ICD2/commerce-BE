package com.emotionalcart.order.presentation.client;

import com.emotionalcart.order.presentation.client.dto.ProductStockRequest;
import com.emotionalcart.order.presentation.client.dto.UpdateStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 상품 호출 API
 *
 * @author yeji cho
 * @since 2024.1.12
 **/
@FeignClient(name = "product-service", url = "${product.find.feign-endpoint}", path = "/api/v1/product")
public interface ProductFeignClient {

    /**
     * 상품 재고 조회
     *
     * @return
     */
    @PostMapping("/stock")
    <T> ResponseEntity<T> getProductStock(@RequestBody ProductStockRequest request);

    /**
     * 상품 재고 변경
     *
     * @return
     */
    @PutMapping("/stock")
    <T> ResponseEntity<T> updateProductStock(@RequestBody UpdateStockRequest request);

}
