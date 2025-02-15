package com.emotionalcart.member.infrasturcture.auth.http;

import com.emotionalcart.member.infrasturcture.auth.dto.ReadDecodeJwtTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "${auth.find.feign-endpoint}", configuration = FeignClientConfig.class)
public interface AuthFeignClient {
    @GetMapping("/api/v1/auth/decode-jwt-token")
    ReadDecodeJwtTokenResponse decodeJwtToken(@RequestHeader("Authorization") String authorizationHeader);
}
