package com.emotionalcart.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String memberId;

    @NotNull
    private String recipientName;

    @NotNull
    private String recipientPhone;

    @NotNull
    private String postalCode;

    @NotNull
    private String defaultAddress;

    @NotNull
    private String detailAddress;

    @NotNull
    private String commonPassword;

    private Boolean isDefault = false;

    private Address(
            String memberId,
            String recipientName,
            String recipientPhone,
            String postalCode,
            String defaultAddress,
            String detailAddress,
            String commonPassword,
            Boolean isDefault
    ) {
        this.memberId = memberId;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.postalCode = postalCode;
        this.defaultAddress = defaultAddress;
        this.detailAddress = detailAddress;
        this.commonPassword = commonPassword;
        this.isDefault = isDefault;
    }

    public static Address of(
            String memberId,
            String recipientName,
            String recipientPhone,
            String postalCode,
            String defaultAddress,
            String detailAddress,
            String commonPassword,
            Boolean isDefault
    ) {
        return new Address(
                memberId,
                recipientName,
                recipientPhone,
                postalCode,
                defaultAddress,
                detailAddress,
                commonPassword,
                isDefault
        );
    }
}
