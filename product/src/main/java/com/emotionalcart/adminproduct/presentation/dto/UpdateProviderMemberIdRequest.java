package com.emotionalcart.adminproduct.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProviderMemberIdRequest {

    private Long providerId;
    private Long memberId;
}
