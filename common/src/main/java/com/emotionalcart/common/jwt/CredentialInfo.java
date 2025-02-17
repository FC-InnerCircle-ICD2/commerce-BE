package com.emotionalcart.common.jwt;

import lombok.Data;

@Data
public class CredentialInfo {

    private Long credential;

    public CredentialInfo(Long id) {
        this.credential = id;
    }

}