package com.emotionalcart.auth.presentation;

import com.emotionalcart.auth.application.AuthService;
import com.emotionalcart.auth.presentation.dto.DecodeJwtTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/decode-jwt-token")
    public ResponseEntity<DecodeJwtTokenResponse> decodeJwtToken(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader
    ) {
        return ResponseEntity.ok(authService.decodeJwtToken(authorizationHeader));
    }
}
