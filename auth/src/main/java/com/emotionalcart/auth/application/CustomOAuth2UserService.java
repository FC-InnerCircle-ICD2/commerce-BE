package com.emotionalcart.auth.application;


import com.emotionalcart.auth.application.dto.*;
import com.emotionalcart.auth.domain.CustomOAuth2User;
import com.emotionalcart.auth.infrasturcture.MemberRepository;
import com.emotionalcart.core.feature.Member;
import com.emotionalcart.core.feature.enums.MemberState;
import com.emotionalcart.core.feature.enums.SocialType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 어떤 소셜 로그인 구분하기 위한 ID
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;
        if("naver".equals(registrationId)) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }else if("kakao".equals(registrationId)) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        // 리소스 서버에서 발급 받은 정보로 소셜로그인 특정할 수 있는 아이디값
        String socialId = oAuth2Response.getProviderId();

        // SocialType 매핑
        SocialType socialType;
        try {
            socialType = SocialType.fromString(oAuth2Response.getProvider());
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException("Unsupported social type: " + oAuth2Response.getProvider());
        }

        System.out.println("oAuth2Response.getProviderId() = " + socialId);
        Member member = memberRepository.findBySocialId(socialId);

        if(member == null) {
            Member insertMember = Member.of(
                    socialId,
                    oAuth2Response.getEmail(),
                    oAuth2Response.getName(),
              null,
                    socialType,
                    MemberState.ACTIVE
            );

            memberRepository.save(insertMember);
        }

        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setName(oAuth2Response.getName());
        memberResponse.setUserName(oAuth2Response.getName());
        memberResponse.setRole("COMMERCE_MEMBER");

        return new CustomOAuth2User(memberResponse);
    }
}
