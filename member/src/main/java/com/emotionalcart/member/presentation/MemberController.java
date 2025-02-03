package com.emotionalcart.member.presentation;

import com.emotionalcart.member.application.MemberService;
import com.emotionalcart.member.application.dto.MemberRequest;
import com.emotionalcart.member.presentation.dto.AuthMembers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/findOrCreateMember")
    public ResponseEntity<AuthMembers.Response>findOrCreateMember(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(memberService.findOrCreateMember(request.getSocialId(), request.getName(), request.getSocialType()));
    }
}
