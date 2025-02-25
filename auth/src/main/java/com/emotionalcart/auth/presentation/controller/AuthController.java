package com.emotionalcart.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/uri/naver")
    public ResponseEntity<String> getNaverAuthUri() {
        return ResponseEntity.ok("/oauth2/authorization/naver");
    }

    @GetMapping("/uri/kakao")
    public ResponseEntity<String> getKakaoAuthUri() {
        return ResponseEntity.ok("/oauth2/authorization/kakao");
    }

}
