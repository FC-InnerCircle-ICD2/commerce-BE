package com.emotionalcart.member.domain;

import com.emotionalcart.core.feature.Member;
import com.emotionalcart.core.feature.enums.SocialType;
import com.emotionalcart.member.infrasturcture.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDataProvider {

    private final MemberRepository memberRepository;

    public Member findOrCreate(String socialId, String name, SocialType socialType) {
        // 소셜 ID로 회원 검색
        return memberRepository.findBySocialId(socialId)
            .orElseGet(() -> {
                Member newMember = Member.of(
                    socialId,
                    name,
                    socialType
                );
                return memberRepository.save(newMember);
            });
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

}
