package com.emotionalcart.member.presentation.dto;

import com.emotionalcart.core.feature.enums.MemberState;
import com.emotionalcart.member.application.UpdateAdminMember;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UpdateAdminMemberRequest {

    @Schema(description = "회원 상태", example = "ACTIVE")
    private MemberState memberState;

    public UpdateAdminMember mapToCommand() {
        return UpdateAdminMember.builder()
            .memberState(memberState)
            .build();
    }
}
