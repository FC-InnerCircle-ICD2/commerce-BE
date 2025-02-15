package com.emotionalcart.core.feature;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.enums.SocialType;
import com.emotionalcart.core.feature.enums.MemberState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String socialId;

    @NotNull
    private String userName;

    private String nickName;

    private String phone;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MemberState memberState;

    private Member(
            String socialId,
            String userName,
            String nickName,
            String phone,
            SocialType socialType,
            MemberState memberState
    ) {
        this.socialId = socialId;
        this.userName = userName;
        this.nickName = nickName;
        this.phone = phone;
        this.socialType = socialType;
        this.memberState = memberState;
    }

    public static Member of(
            String socialId,
            String userName,
            SocialType socialType
    ) {
        return new Member(
                socialId,
                userName,
                null,
                null,
                socialType,
                MemberState.ACTIVE
        );
    }
}
