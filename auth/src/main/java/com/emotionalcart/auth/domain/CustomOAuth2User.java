package com.emotionalcart.auth.domain;

import com.emotionalcart.auth.application.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberResponse memberResponse;

    @Override
    public Map<String, Object> getAttributes() {
        // 소셜 로그인마다 return 해주는 방식이 달라서 미사용
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberResponse.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return memberResponse.getName();
    }

    public String getUserName() {
        return memberResponse.getUserName();
    }
}
