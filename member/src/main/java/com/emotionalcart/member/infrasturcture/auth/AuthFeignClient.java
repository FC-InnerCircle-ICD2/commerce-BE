package com.emotionalcart.member.infrasturcture.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "${auth.find.feign-endpoint}")
public interface AuthFeignClient {
    @GetMapping("/api/v1/auth/decode-jwt-token")
    ResponseEntity<Long> decodeJwtToken(@RequestHeader("Authorization") String authorizationHeader);
}
