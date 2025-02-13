package com.emotionalcart.order.infra.product;

import com.emotionalcart.order.infra.product.dto.*;
import com.emotionalcart.order.infra.product.http.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductFeignClient productFeignClient;

    /**
     * 상품 가격 조회
     */
    public List<ProductPrice> getProductPrice(List<ProductPriceRequest> productPriceRequest) {
        ResponseEntity<List<ProductPriceResponse>> response = productFeignClient.getProductPrice(productPriceRequest);
        List<ProductPriceResponse> productPriceResponse = response.getBody();
        assert productPriceResponse != null;
        return productPriceResponse.stream().map(ProductPrice::convert).toList();
    }

    /**
     * 상품 유효성 검사
     *
     * @param productValidationRequest
     * @return
     */
    public void isValidProduct(List<ProductValidationRequest> productValidationRequest) {
        productFeignClient.validateProductPrice(productValidationRequest);
    }

    /**
     * 상품 재고 변경
     *
     * @param productStockRequest
     * @return
     */
    public void updateProductStock(List<ProductStockRequest> productStockRequest) {
        productFeignClient.updateProductStock(productStockRequest);
    }

}
