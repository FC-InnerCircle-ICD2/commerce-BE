package com.emotionalcart.member.infrasturcture.product.http;

import com.emotionalcart.member.infrasturcture.config.FeignClientConfig;
import com.emotionalcart.member.infrasturcture.product.dto.ReadProviderResponse;
import com.emotionalcart.member.infrasturcture.product.dto.UpdateProviderMemberIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service", url = "${product.find.feign-endpoint}", path = "/api/admin/v1/provider", configuration = FeignClientConfig.class)
public interface ProductFeignClient {

    /**
     * 계정의 판매처 (계정) 정보 조회
     *
     * @param providerId
     * @return
     */
    @GetMapping("/{providerId}")
    ResponseEntity<ReadProviderResponse> readProvider(@PathVariable Long providerId);

    /**
     *
     * @param request
     */
    @PutMapping("/memberId")
    void updateProviderMemberId(@RequestBody UpdateProviderMemberIdRequest request);

}
