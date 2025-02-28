package com.emotionalcart.member.infrasturcture.product.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadProvider {

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


    public ReadProvider(ReadProviderResponse providerInfoResponse) {
        this.providerId = providerInfoResponse.getProviderId();
        this.name = providerInfoResponse.getName();
        this.description = providerInfoResponse.getDescription();
        this.memberId = providerInfoResponse.getMemberId();
    }

    public static ReadProvider convert(ReadProviderResponse providerInfoResponse) {
        return new ReadProvider(providerInfoResponse);
    }

}
