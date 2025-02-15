package com.emotionalcart.auth.domain;

import com.emotionalcart.auth.application.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberResponse memberResponse;

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", memberResponse.getUserId());
        attributes.put("userName", memberResponse.getName());
        attributes.put("role", memberResponse.getRole());
        return attributes;
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

    public Long getUserId() {
        return memberResponse.getUserId();
    }

    @Override
    public String getName() {
        return memberResponse.getName();
    }
}
