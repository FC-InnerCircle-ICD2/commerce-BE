package com.emotionalcart.member.application.dto;

import com.emotionalcart.core.feature.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private String socialId;
    private String name;
    private SocialType socialType;
}
