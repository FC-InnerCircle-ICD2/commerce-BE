package com.emotionalcart.auth.presentation.handler;

import com.emotionalcart.auth.domain.CustomOAuth2User;
import com.emotionalcart.common.jwt.JwtProperties;
import com.emotionalcart.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // OAuth2User
        CustomOAuth2User oauth2User = (CustomOAuth2User)authentication.getPrincipal();
        String username = oauth2User.getName();
        Long userId = oauth2User.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream().map(r -> "ROLE_" + r).toList();
        String accessToken = jwtTokenProvider.createAccessToken(userId, username, roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, username, roles);

        // 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
    }

}
