package com.emotionalcart.core.feature.enums;

import java.util.Arrays;

public enum SocialType {
    KAKAO,
    NAVER,
    GOOGLE,
    APPLE;

    public static SocialType fromString(String provider) {
        return Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.name().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported social type: " + provider));
    }
}
