package com.emotionalcart.member.application;

import com.emotionalcart.common.jwt.JwtTokenProvider;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.MemberException;
import com.emotionalcart.core.feature.Member;
import com.emotionalcart.core.feature.enums.MemberRole;
import com.emotionalcart.core.feature.enums.MemberState;
import com.emotionalcart.core.feature.enums.SocialType;
import com.emotionalcart.member.infrasturcture.MemberRepository;
import com.emotionalcart.member.infrasturcture.product.ProductService;
import com.emotionalcart.member.infrasturcture.product.dto.ReadProvider;
import com.emotionalcart.member.infrasturcture.product.dto.UpdateProviderMemberIdRequest;
import com.emotionalcart.member.presentation.dto.AdminMembersResponse;
import com.emotionalcart.member.presentation.dto.CreateProviderAdminMemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProductService productService;

    public boolean createAdminUser(CreateAdminMember createAdminMember) {
        memberRepository.findByEmail(createAdminMember.getEmail())
            .ifPresent(member -> {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            });
        Member member = Member.builder()
            .nickName(createAdminMember.getUserName())
            .userName(createAdminMember.getUserName())
            .email(createAdminMember.getEmail())
            .password(passwordEncoder.encode(createAdminMember.getPassword()))
            .phone(createAdminMember.getPhone())
            .memberState(MemberState.ACTIVE)
            .socialType(SocialType.DIRECT)
            .memberRoles(Set.of(MemberRole.COMMERCE_MEMBER, MemberRole.ADMIN_MEMBER))
            .build();
        memberRepository.save(member);
        return true;
    }

    public TokenInfo login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        List<String> roles = member.getMemberRoles().stream().map(MemberRole::name).toList();
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(),
                                                                member.getEmail(),
                                                                roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), member.getEmail(), roles);
        return TokenInfo.of(accessToken, refreshToken);
    }

    /**
     * 관리자 계정 조회
     * 현재는 시스템 관리자 기준으로 개발
     * 추후에는 업체도 고려해서 개발 필요
     *
     * @return
     */
    @Transactional(readOnly = true)
    public void validateAdminUser(Long memberId) {
        memberRepository.findByIdAndMemberRoles(memberId, MemberRole.ADMIN_MEMBER)
            .orElseThrow(()-> new IllegalArgumentException("올바른 관리자 계정으로 로그인 해주세요."));
    }

    @Transactional(readOnly = true)
    public Page<Member> getAdminMembers(Long jwtId, Pageable pageable) {
        validateAdminUser(jwtId);
        return memberRepository.findAllByMemberRoles(MemberRole.ADMIN_MEMBER, pageable);
    }

    @Transactional(readOnly = true)
    public Member getAdminMemberInfo(Long jwtId, Long memberId) {
        validateAdminUser(jwtId);
        return memberRepository.findByIdAndMemberRoles(memberId, MemberRole.ADMIN_MEMBER)
            .orElseThrow(() -> new MemberException(ErrorCode.INVALID_ADMIN_MEMBER));
    }

    @Transactional
    public Member updateAdminMemberInfo(Long jwtId, Long memberId, UpdateAdminMember updateAdminMember) {
        validateAdminUser(jwtId);
        Member member = memberRepository.findByIdAndMemberRoles(memberId, MemberRole.ADMIN_MEMBER)
            .orElseThrow(() -> new MemberException(ErrorCode.INVALID_ADMIN_MEMBER));
        member.changeAdminMemberInfo(updateAdminMember.getMemberState());
        memberRepository.save(member);
        return member;
    }

    public AdminMembersResponse createProviderAdminUser(Long id, CreateProviderAdminMemberRequest request) {

        log.info("entered provider id: {}", request.getProviderId());

        ReadProvider provider = productService.getProviderInfo(request.getProviderId());
        log.info("provider id: {}, memberId: {}", provider.getProviderId(), provider.getMemberId());

        if(provider.getMemberId() != null) {
            throw new IllegalArgumentException("이미 발급된 관리자 계정이 존재합니다.");
        }
        CreateAdminMember createAdminMember = request.mapToCommand();

        memberRepository.findByEmail(createAdminMember.getEmail())
            .ifPresent(member -> {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            });
        Member member = Member.builder()
            .nickName(createAdminMember.getUserName())
            .userName(createAdminMember.getUserName())
            .email(createAdminMember.getEmail())
            .password(passwordEncoder.encode(createAdminMember.getPassword()))
            .phone(createAdminMember.getPhone())
            .memberState(MemberState.ACTIVE)
            .socialType(SocialType.DIRECT)
            .memberRoles(Set.of(MemberRole.PROVIDER_MEMBER))
            .build();


        memberRepository.save(member);
        productService.updateProviderMemberId(UpdateProviderMemberIdRequest.of(provider.getProviderId(), member.getId()));

        return AdminMembersResponse.from(member);
    }

}
