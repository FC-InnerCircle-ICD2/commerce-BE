package com.emotionalcart.common.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHeaderValidator {

    private final JwtProperties jwtProperties;
    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    public Optional<String> obtainAuthorizationToken(HttpServletRequest req) {
        Optional<String> optionalToken = Optional.ofNullable(req.getHeader(jwtProperties.getHeaderKey()));
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
