package com.emotionalcart.auth.presentation;

import com.emotionalcart.core.config.jwt.JwtUtil;
import com.emotionalcart.core.exception.AuthException;
import com.emotionalcart.core.exception.ErrorCode;
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
    private final JwtUtil jwtUtil;

    @GetMapping("/decode-jwt-token")
    public ResponseEntity<Long> decodeJwtToken(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        // 헤더 존재 여부 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthException(ErrorCode.MISSING_TOKEN);
        }

        // 토큰 추출
        String token = authorizationHeader.substring(7);

        // 토큰 만료 여부 검증
        if (jwtUtil.isExpired(token)) {
            throw new AuthException(ErrorCode.TOKEN_EXPIRED);
        }

        // 사용자 아이디 반환
        Long userId = jwtUtil.getUserId(token);
        return ResponseEntity.ok(userId);
    }
}
