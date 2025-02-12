package com.emotionalcart.member.application;

import com.emotionalcart.core.feature.Member;
import com.emotionalcart.core.feature.enums.SocialType;
import com.emotionalcart.member.domain.MemberDataProvider;
import com.emotionalcart.member.presentation.dto.AuthMembers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDataProvider memberDataProvider;

    public AuthMembers.Response findOrCreate(String socialId, String name, SocialType socialType) {
        Member member = memberDataProvider.findOrCreate(socialId, name, socialType);

        return new AuthMembers.Response("COMMERCE_MEMBER", member.getUserName(), member.getSocialId());
    }
}
