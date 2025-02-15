package com.emotionalcart.auth.application;

import com.emotionalcart.auth.presentation.dto.DecodeJwtTokenResponse;
import com.emotionalcart.core.config.jwt.JwtUtil;
import com.emotionalcart.core.exception.AuthException;
import com.emotionalcart.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;

    public DecodeJwtTokenResponse decodeJwtToken(String authorizationHeader) {
        // 헤더 존재 여부 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthException(ErrorCode.UNAUTHORIZED);
        }

        // 토큰 추출
        String token = authorizationHeader.substring(7);

        // 토큰 만료 여부 검증
        if (jwtUtil.isExpired(token)) {
            throw new AuthException(ErrorCode.TOKEN_EXPIRED);
        }

        // 사용자 정보 반환
        Long userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);
        return new DecodeJwtTokenResponse(userId, role);
    }
}
