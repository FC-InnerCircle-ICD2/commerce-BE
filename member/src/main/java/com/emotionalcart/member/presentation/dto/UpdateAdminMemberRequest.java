package com.emotionalcart.member.presentation.dto;

import com.emotionalcart.core.feature.enums.MemberState;
import com.emotionalcart.member.application.UpdateAdminMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UpdateAdminMemberRequest {

    @Schema(description = "회원 상태", example = "ACTIVE")
    private String memberState;

    public UpdateAdminMember mapToCommand() {
        MemberState state;
        try {
            state = MemberState.valueOf(memberState.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("부적절한 입력 값입니다.");
        }
        return UpdateAdminMember.builder()
            .memberState(state)
            .build();
    }
}
