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
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private final JwtProperties jwtProperties;

    private final JwtTokenProvider tokenProvider;

    private final JwtHeaderValidator jwtHeaderValidator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<String> optionalAuthorizationToken = jwtHeaderValidator.obtainAuthorizationToken(req);
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
        Set<MemberRole> roles = tokenProvider.getRoles(authorizationToken).stream().map(MemberRole::valueOf).collect(Collectors.toSet());

        if (nonNull(id) && !isEmpty(username)) {
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(new JwtAuthentication(id, username, roles), null,
                                                                               this.authorities(authorizationToken));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    public Collection<? extends GrantedAuthority> authorities(String token) {
        List<String> roles = tokenProvider.getRoles(token);
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toSet());
    }

}