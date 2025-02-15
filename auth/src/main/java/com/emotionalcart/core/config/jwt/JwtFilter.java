package com.emotionalcart.core.config.jwt;

import com.emotionalcart.auth.domain.CustomOAuth2User;
import com.emotionalcart.auth.application.dto.MemberResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Authorization 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        // "Bearer "를 제거하고 JWT 토큰만 추출
        String token = authorizationHeader.substring(7);

        // 토큰 유효성 검증
        if (jwtUtil.isExpired(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return;
        }

        // 토큰에서 사용자 이름 정보 추출
        String userName = jwtUtil.getUserName(token);

        // 사용자 정보를 담아서 생성
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setName(userName);
        memberResponse.setRole("COMMERCE_MEMBER");

        // CustomOAuth2User 객체 생성
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberResponse);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // SecurityContextHolder에 사용자 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
