package com.emotionalcart.order.infra.product.http;

import com.emotionalcart.order.infra.config.FeignClientConfig;
import com.emotionalcart.order.infra.product.dto.ProductDetailResponse;
import com.emotionalcart.order.infra.product.dto.ProductPriceRequest;
import com.emotionalcart.order.infra.product.dto.ProductPriceResponse;
import com.emotionalcart.order.infra.product.dto.ProductQuantityValidateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${product.find.feign-endpoint}", path = "/api/v1/products", configuration = FeignClientConfig.class)
public interface ProductFeignClient {

    /**
     * 상품 가격 조회
     *
     * @param productPriceRequest
     * @return
     */
    @PostMapping("/price")
    ResponseEntity<List<ProductPriceResponse>> getProductPrice(@RequestBody List<ProductPriceRequest> productPriceRequest);

    /**
     * 상품 검증
     *
     * @param productQuantityValidateRequest
     * @return
     */
    @PostMapping("/validate")
    ResponseEntity<Void> validateProductPrice(@RequestBody List<ProductQuantityValidateRequest> productQuantityValidateRequest);

    /**
     * 상품 목록 조회
     */
    @GetMapping("/{productId}")
    ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long productId);

}
