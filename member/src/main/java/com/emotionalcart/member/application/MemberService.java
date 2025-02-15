package com.emotionalcart.member.application;

import com.emotionalcart.core.feature.Member;
import com.emotionalcart.member.infrasturcture.auth.AuthFeignClient;
import com.emotionalcart.member.presentation.dto.MemberRequest;
import com.emotionalcart.member.domain.MemberDataProvider;
import com.emotionalcart.member.presentation.dto.MemberResponse;
import com.emotionalcart.member.presentation.dto.ReadMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDataProvider memberDataProvider;
    private final AuthFeignClient authFeignClient;

    @Transactional
    public MemberResponse findOrCreate(MemberRequest request) {
        Member member = memberDataProvider.findOrCreate(request.getSocialId(), request.getName(), request.getSocialType());

        return new MemberResponse(
                member.getId(),
                "COMMERCE_MEMBER",
                member.getUserName()
        );
    }

    public ReadMemberResponse readMember(String authorizationHeader) {
        Long memberId = authFeignClient.decodeJwtToken(authorizationHeader).getBody();
        Member member = memberDataProvider.findMember(memberId);
        return new ReadMemberResponse(member.getId(), "COMMERCE_MEMBER", member.getUserName());
    }
}
