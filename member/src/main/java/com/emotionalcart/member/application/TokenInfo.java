package com.emotionalcart.member.application;

import lombok.Getter;

@Getter
public class TokenInfo {

    private String accessToken;

    private String refreshToken;

    public static TokenInfo of(String accessToken, String refreshToken) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.accessToken = accessToken;
        tokenInfo.refreshToken = refreshToken;
        return tokenInfo;
    }

}
