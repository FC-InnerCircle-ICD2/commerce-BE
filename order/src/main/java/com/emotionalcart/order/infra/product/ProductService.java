package com.emotionalcart.order.infra.product;

import com.emotionalcart.order.infra.advice.exceptions.ProductPriceException;
import com.emotionalcart.order.infra.advice.exceptions.ProductStockException;
import com.emotionalcart.order.infra.advice.exceptions.ProductValidationException;
import com.emotionalcart.order.infra.product.dto.*;
import com.emotionalcart.order.infra.product.http.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductFeignClient productFeignClient;

    /**
     * 상품 가격 조회
     */
    public List<ProductPrice> getProductPrice(List<ProductPriceRequest> productPriceRequest) {
        ResponseEntity response = productFeignClient.getProductPrice(productPriceRequest);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ProductPriceException(Objects.requireNonNull(response.getBody()).toString());
        }
        List<ProductPriceResponse> productPriceResponse = (List<ProductPriceResponse>)response.getBody();
        assert productPriceResponse != null;
        return productPriceResponse.stream().map(ProductPrice::convert).toList();
    }

    /**
     * 상품 유효성 검사
     *
     * @param productValidationRequest
     * @return
     */
    public boolean isValidProduct(List<ProductValidationRequest> productValidationRequest) {
        ResponseEntity response = productFeignClient.validateProductPrice(productValidationRequest);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ProductValidationException(Objects.requireNonNull(response.getBody()).toString());
        }
        return true;
    }

    /**
     * 상품 재고 변경
     *
     * @param productStockRequest
     * @return
     */
    public boolean updateProductStock(List<ProductStockRequest> productStockRequest) {
        ResponseEntity response = productFeignClient.updateProductStock(productStockRequest);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ProductStockException(Objects.requireNonNull(response.getBody()).toString());
        }
        return true;
    }

}
