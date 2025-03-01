package com.emotionalcart.member.infrasturcture.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProviderMemberIdRequest {

    /**
     * 판매처 아이디
     */
    private Long providerId;

    /**
     * 판매처 등록된 Member 아이디
     */
    private Long memberId;

    public static UpdateProviderMemberIdRequest of(
        Long providerId,
        Long memberId
    ) {
        UpdateProviderMemberIdRequest request = new UpdateProviderMemberIdRequest();
        request.providerId = providerId;
        request.memberId = memberId;
        return request;
    }

}
