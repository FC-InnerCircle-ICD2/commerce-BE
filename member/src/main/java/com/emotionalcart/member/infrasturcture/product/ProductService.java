package com.emotionalcart.member.infrasturcture.product;

import com.emotionalcart.member.infrasturcture.product.dto.ReadProvider;
import com.emotionalcart.member.infrasturcture.product.dto.ReadProviderResponse;
import com.emotionalcart.member.infrasturcture.product.dto.UpdateProviderMemberIdRequest;
import com.emotionalcart.member.infrasturcture.product.http.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductFeignClient productFeignClient;

    /**
     * 판매처 정보 조회
     */
    public ReadProvider getProviderInfo(Long providerId) {
        ResponseEntity<ReadProviderResponse> response = productFeignClient.readProvider(providerId);
        ReadProviderResponse responseBody = response.getBody();
        return ReadProvider.convert(responseBody);
    }

    public void updateProviderMemberId(UpdateProviderMemberIdRequest request) {
        productFeignClient.updateProviderMemberId(request);
    }
}
