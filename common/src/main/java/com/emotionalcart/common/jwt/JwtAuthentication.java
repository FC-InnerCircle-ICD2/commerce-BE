package com.emotionalcart.common.jwt;

import java.util.Set;

public record JwtAuthentication(Long id, String name, Set<MemberRole> roles) {

}