package com.emotionalcart.common.jwt;

import java.util.List;

public record Member(
    Long userId,
    List<String> roles,
    String name) {

}
