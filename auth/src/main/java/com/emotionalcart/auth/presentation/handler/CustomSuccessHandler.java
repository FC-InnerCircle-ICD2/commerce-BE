package com.emotionalcart.auth.presentation.handler;

import com.emotionalcart.auth.domain.CustomOAuth2User;
import com.emotionalcart.common.jwt.JwtProperties;
import com.emotionalcart.common.jwt.JwtTokenProvider;
import com.emotionalcart.core.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws
        IOException {
        HttpSession session = request.getSession();
        String redirectUri = (String)session.getAttribute("redirect_uri");
        session.removeAttribute("redirect_uri"); // 사용 후 제거

        log.info("Redirect URI: {}", redirectUri);

        if (redirectUri == null || redirectUri.isEmpty()) {
            redirectUri = "https://www.emmotional-cart.click";
        }

        // OAuth2User
        CustomOAuth2User oauth2User = (CustomOAuth2User)authentication.getPrincipal();
        String username = oauth2User.getName();
        Long userId = oauth2User.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream().map(r -> "ROLE_" + r).toList();
        String accessToken = jwtTokenProvider.createAccessToken(userId, username, roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, username, roles);

        // 응답 설정
        CookieUtil.addCookie(response, "Access-Token", accessToken, 10 * 60 * 60);
        CookieUtil.addCookie(response, "Refresh-Token", refreshToken, 30 * 60);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.sendRedirect(redirectUri);
    }

}
