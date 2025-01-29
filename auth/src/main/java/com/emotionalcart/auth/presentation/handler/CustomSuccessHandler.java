package com.emotionalcart.auth.presentation.handler;

import com.emotionalcart.auth.application.dto.AuthenticationResponse;
import com.emotionalcart.auth.domain.CustomOAuth2User;
import com.emotionalcart.core.config.jwt.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getUserName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 생성 (1시간 유효시간)
        String token = jwtUtil.createJwt(username, role, 60 * 60L);

        // 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Authorization", "Bearer " + token);

        // 토큰을 JSON 형식으로 클라이언트에 반환
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationResponse authResponse = new AuthenticationResponse(token);
        response.getWriter().write(objectMapper.writeValueAsString(authResponse));

        // 클라이언트 쪽에서 토큰을 저장 후 리다이렉션 처리

    }
}
