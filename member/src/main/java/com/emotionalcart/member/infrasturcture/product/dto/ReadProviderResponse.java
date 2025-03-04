package com.emotionalcart.member.infrasturcture.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 가격 응답값
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ReadProviderResponse {

    /**
     * 판매처 아이디
     */
    private Long providerId;

    /**
     * 가격
     */
    private String name;

    /**
     * 설명
     */
    private String description;

    /**
     * 판매처의 관리자 계정 아이디
     */
    private Long memberId;
}
