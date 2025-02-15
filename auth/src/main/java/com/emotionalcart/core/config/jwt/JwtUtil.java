package com.emotionalcart.core.config.jwt;

import com.emotionalcart.core.exception.AuthException;
import com.emotionalcart.core.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String jwtSecretKey = "vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbb";
    private SecretKey secretKey;

    public JwtUtil() {
        secretKey = new SecretKeySpec(jwtSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Long getUserId(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
        } catch (JwtException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getUserName(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
        } catch (JwtException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getRole(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
        } catch (JwtException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Boolean isExpired(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (JwtException e) {
            throw new AuthException(ErrorCode.TOKEN_EXPIRED);
        }
    }


    public String createJwt(Long userId, String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}