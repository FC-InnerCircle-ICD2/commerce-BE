package com.emotionalcart.member.application;

import com.emotionalcart.core.feature.Member;
import com.emotionalcart.member.domain.MemberDataProvider;
import com.emotionalcart.member.presentation.dto.MemberRequest;
import com.emotionalcart.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDataProvider memberDataProvider;

    @Transactional
    public MemberResponse findOrCreate(MemberRequest request) {
        Member member = memberDataProvider.findOrCreate(request.getSocialId(), request.getName(), request.getSocialType());

        return new MemberResponse(
            member.getId(),
            member.getMemberRoles().stream().map(Enum::name).toList(),
            member.getUserName()
        );
    }

    public MemberResponse getMemberInfo(Long id) {
        Member member = memberDataProvider.findById(id);
        return new MemberResponse(
            member.getId(),
            member.getMemberRoles().stream().map(Enum::name).toList(),
            member.getUserName()
        );
    }

}
