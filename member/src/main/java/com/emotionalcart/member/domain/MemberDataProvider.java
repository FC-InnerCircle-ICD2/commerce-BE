package com.emotionalcart.member.domain;

import com.emotionalcart.core.feature.Member;
import com.emotionalcart.core.feature.enums.MemberState;
import com.emotionalcart.core.feature.enums.SocialType;
import com.emotionalcart.member.infrasturcture.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDataProvider {
    private final MemberRepository memberRepository;

    public Member findOrCreateMember(String socialId, String name, SocialType socialType) {
        // 소셜 ID로 회원 검색
        Member member = memberRepository.findBySocialId(socialId);

        // 회원 없으면 새로 생성
        if (member == null) {
            Member insertMember = Member.of(
                    socialId,
                    name,
                    null,
                    null,
                    socialType,
                    MemberState.ACTIVE
            );

            memberRepository.save(insertMember);
            member = insertMember;
        }

        return member;
    }
}
