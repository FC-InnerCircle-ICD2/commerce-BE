package com.emotionalcart.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private final JwtProperties jwtProperties;

    private final JwtTokenProvider tokenProvider;

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<String> optionalAuthorizationToken = obtainAuthorizationToken(req, jwtProperties.getHeaderKey());
            if (optionalAuthorizationToken.isPresent()) {
                String authorizationToken = optionalAuthorizationToken.get();
                try {
                    setNewAuthenticationToken(req, authorizationToken);
                } catch (Exception e) {
                    log.error("JWT processing failed : {}", e.getMessage());
                }
            }
        } else {
            log.debug("SecurityContextHolder 에 security 토큰이 생성되지 않았습니다, 이미 저장된 인증정보 : '{}'",
                      SecurityContextHolder.getContext().getAuthentication());
        }
        chain.doFilter(req, res);
    }

    private void setNewAuthenticationToken(HttpServletRequest req, String authorizationToken) {
        Long id = tokenProvider.getId(authorizationToken);
        String username = tokenProvider.getUsername(authorizationToken);

        if (nonNull(id) && !isEmpty(username)) {
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(new JwtAuthentication(id, username), null,
                                                                               this.authorities(authorizationToken));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    public Collection<? extends GrantedAuthority> authorities(String token) {
        List<String> roles = tokenProvider.getRoles(token);
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toSet());
    }

    public static Optional<String> obtainAuthorizationToken(HttpServletRequest req, String headerKey) {
        Optional<String> optionalToken = Optional.ofNullable(req.getHeader(headerKey));
        if (optionalToken.isPresent()) {
            String token = optionalToken.get();
            if (log.isDebugEnabled()) {
                log.debug("JWT 인증 요청이 들어 왔습니다. 토큰 : {}", token);
            }
            token = URLDecoder.decode(token, StandardCharsets.UTF_8);
            String[] parts = token.split(" ");
            if (parts.length == 2) {
                String scheme = parts[0];
                String credentials = parts[1];
                return BEARER.matcher(scheme).matches() ? Optional.ofNullable(credentials) : Optional.empty();
            }
        }
        return Optional.empty();
    }

}