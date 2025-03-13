package com.emotionalcart.member.presentation;

import com.emotionalcart.common.jwt.JwtAuthentication;
import com.emotionalcart.member.application.MemberService;
import com.emotionalcart.member.presentation.dto.MemberRequest;
import com.emotionalcart.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j @RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/auth/find-or-create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberResponse> findOrCreate(@RequestBody MemberRequest request) {
        log.error("MemberController.findOrCreate request: {}", request);
        return ResponseEntity.ok(memberService.findOrCreate(request));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMemberInfo(@AuthenticationPrincipal JwtAuthentication authentication) {
        return ResponseEntity.ok(memberService.getMemberInfo(authentication.id()));
    }

}
