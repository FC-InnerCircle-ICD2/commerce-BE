package com.emotionalcart.member.application;

import com.emotionalcart.core.feature.enums.MemberState;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateAdminMember {

    MemberState memberState;

}
