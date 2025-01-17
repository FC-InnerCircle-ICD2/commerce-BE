package com.emotionalcart.auth.application.dto;

public interface OAuth2Response {

    // 제공자(ex. 네이버, 카카오 등등)
    String getProvider();

    // 제공자에서 발급해주는 아이디
    String getProviderId();

    // 이메일
    String getEmail();

    // 사용자 설정한 이름
    String getName();
}
