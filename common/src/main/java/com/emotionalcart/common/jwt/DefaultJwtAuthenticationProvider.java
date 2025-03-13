package com.emotionalcart.common.jwt;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 로그인 시 인증 객체를 Authentication 에 담아주는 클래스
 *
 * @author seunggu.lee
 * @see AuthenticationProvider#authenticate(Authentication)
 */
public abstract class DefaultJwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken)authentication;
        return processUserAuthentication((JwtAuthentication)authenticationToken.getPrincipal());
    }

    private Authentication processUserAuthentication(JwtAuthentication principal) {
        try {
            Member account = getMember(principal.id());
            CredentialInfo credentialInfo = new CredentialInfo(principal.id());
            Set<MemberRole> roles = account.roles().stream().map(MemberRole::valueOf).collect(Collectors.toSet());
            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
                new JwtAuthentication(account.userId(), account.name(), roles),
                credentialInfo,
                this.authorities(account.roles()));
            authenticationToken.setDetails(account);
            return authenticationToken;
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

    protected abstract Member getMember(Long accountId);

    private Collection<? extends GrantedAuthority> authorities(List<String> role) {
        return role.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toSet());
    }

}