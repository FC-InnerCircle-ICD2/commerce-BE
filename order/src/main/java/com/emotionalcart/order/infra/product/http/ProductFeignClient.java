package com.emotionalcart.order.infra.product.http;

import com.emotionalcart.order.infra.product.dto.ProductPriceRequest;
import com.emotionalcart.order.infra.product.dto.ProductStockRequest;
import com.emotionalcart.order.infra.product.dto.ProductValidationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${product.find.feign-endpoint}", path = "/api/v1/products")
public interface ProductFeignClient {

    /**
     * 상품 가격 조회
     *
     * @param productPriceRequest
     * @param <T>
     * @return
     */
    @PostMapping("/price")
    <T> ResponseEntity<T> getProductPrice(@RequestBody List<ProductPriceRequest> productPriceRequest);

    /**
     * 상품 검증
     *
     * @param productValidationRequest
     * @param <T>
     * @return
     */
    @PostMapping("/validate")
    <T> ResponseEntity<T> validateProductPrice(@RequestBody List<ProductValidationRequest> productValidationRequest);

    /**
     * 상품 재고 변경
     *
     * @param productStockRequest
     * @param <T>
     * @return
     */
    @PostMapping("/stock")
    <T> ResponseEntity<T> updateProductStock(@RequestBody List<ProductStockRequest> productStockRequest);

}
