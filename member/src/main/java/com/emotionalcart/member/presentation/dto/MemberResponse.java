package com.emotionalcart.member.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private Long userId;
    private List<String> roles;
    private String name;

}
