package com.emotionalcart.common.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(jwtProperties.getSecretKey().getBytes(), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createAccessToken(Long userId, String username, List<String> roles) {
        long tokenValidationSecond = 1000L * 60 * 60 * 10;
        return createToken(userId, username, roles, tokenValidationSecond);
    }

    public String createRefreshToken(Long userId, String username, List<String> roles) {
        // 30분 동안 유효한 리프레시 토큰
        long refreshTokenValidationSecond = 1000L * 60 * 30;
        return createToken(userId, username, roles, refreshTokenValidationSecond);
    }

    public String createToken(Long userId, String username, List<String> roles, long expireTime) {
        Claims claims = getClaims(userId, username, roles);
        Date now = new Date();
        return Jwts.builder().claims(claims)  // 데이터
            .issuedAt(now)   // 토큰 발행 일자
            .expiration(new Date(now.getTime() + expireTime))    // 만료 시간 추가
            .signWith(secretKey)          // 암호화 알고리즘, secret 값 세팅
            .compact();
    }

    private Claims getClaims(Long userId, String username, List<String> roles) {
        return Jwts.claims()
            .add("userId", userId)
            .add("username", username)
            .add("roles", roles)
            .build();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String jwtToken) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken).getPayload();
    }

    public Long getId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    public String getUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    public List<String> getRoles(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(getClaims(token).get("roles"), new TypeReference<>() {
        });
    }

}
