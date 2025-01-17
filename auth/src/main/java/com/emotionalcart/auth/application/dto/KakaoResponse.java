package com.emotionalcart.auth.application.dto;


import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        // "kakao_account" 내 "email" 정보를 반환
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        return null;
//        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        // "properties" 내 "nickname" 정보를 반환
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        return properties.get("nickname").toString();
    }

}
