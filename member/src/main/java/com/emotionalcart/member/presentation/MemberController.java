package com.emotionalcart.member.presentation;

import com.emotionalcart.member.application.MemberService;
import com.emotionalcart.member.presentation.dto.MemberRequest;
import com.emotionalcart.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j @RestController
@RequestMapping("/api/v1/members/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/find-or-create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberResponse> findOrCreate(@RequestBody MemberRequest request) {
        log.error("MemberController.findOrCreate request: {}", request);
        return ResponseEntity.ok(memberService.findOrCreate(request));
    }

}
