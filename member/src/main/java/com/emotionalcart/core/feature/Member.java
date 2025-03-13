package com.emotionalcart.core.feature;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.enums.MemberRole;
import com.emotionalcart.core.feature.enums.MemberState;
import com.emotionalcart.core.feature.enums.SocialType;
import com.emotionalcart.core.feature.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @IdGenerator
    private Long id;

    @Column(unique = true)
    private String email;

    private String socialId;

    private String userName;

    private String password;

    private String nickName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private MemberState memberState = MemberState.ACTIVE;

    @ElementCollection(fetch = LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "id"))
    private Set<MemberRole> memberRoles = Set.of(MemberRole.COMMERCE_MEMBER);

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

    public void changeAdminMemberInfo(MemberState memberState) {
        this.memberState = memberState;
    }

}
