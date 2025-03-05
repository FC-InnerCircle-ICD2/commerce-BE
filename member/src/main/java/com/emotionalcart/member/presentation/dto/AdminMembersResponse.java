package com.emotionalcart.member.presentation.dto;

import com.emotionalcart.core.feature.Member;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Tag(name = "백오피스 관리 계정 조회 API", description = "관리자 계정을 조회 하는 API")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "백오피스 관리자 계정 조회 응답")
public class AdminMembersResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "회원 번호", example = "4010584059727534421")
    private Long memberId;

    @Schema(description = "이메일", example = "test2025@test.com")
    private String email;

    @Schema(description = "이름", example = "test2025@test.com")
    private String userName;

    @Schema(description = "연락처", example = "01012341234")
    private String phone;

    @Schema(description = "계정 권한", example = "ADMIN_MEMBER")
    private String memberRoles;

    @Schema(description = "계정 상태", example = "ACTIVE")
    private String memberState;

    public AdminMembersResponse(Long memberId,
                                String email,
                                String userName,
                                String phone,
                                String memberRoles,
                                String memberState) {
        this.memberId = memberId;
        this.email = email;
        this.userName = userName;
        this.phone = phone;
        this.memberRoles = memberRoles;
        this.memberState = memberState;
    }

    public AdminMembersResponse(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.userName = member.getUserName();
        this.phone = member.getPhone();
        this.memberRoles = member.getMemberRoles().toString();
        this.memberState = member.getMemberState().name();
    }

    public static AdminMembersResponse of(Long memberId,
                                          String email,
                                          String userName,
                                          String phone,
                                          String memberRoles,
                                          String memberState) {
        return new AdminMembersResponse(memberId, email, userName, phone, memberRoles, memberState);
    }

    public static AdminMembersResponse from (Member member) {
        return new AdminMembersResponse(member);
    }
}
