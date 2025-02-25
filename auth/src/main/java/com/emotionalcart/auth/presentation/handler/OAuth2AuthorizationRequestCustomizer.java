package com.emotionalcart.auth.presentation.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthorizationRequestCustomizer implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public OAuth2AuthorizationRequestCustomizer(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request);
        return modifyRequest(request, authRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request, clientRegistrationId);
        return modifyRequest(request, authRequest);
    }

    private OAuth2AuthorizationRequest modifyRequest(HttpServletRequest request, OAuth2AuthorizationRequest authRequest) {
        if (authRequest == null) {
            return null;
        }

        // `redirect_uri`를 요청에서 가져와 세션에 저장
        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri != null) {
            request.getSession().setAttribute("redirect_uri", redirectUri);
        }

        return authRequest;
    }
}
