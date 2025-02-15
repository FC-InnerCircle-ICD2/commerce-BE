package com.emotionalcart.member.presentation;

import com.emotionalcart.member.application.MemberService;
import com.emotionalcart.member.presentation.dto.MemberRequest;
import com.emotionalcart.member.presentation.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/find-or-create")
    public ResponseEntity<MemberResponse>findOrCreate(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(memberService.findOrCreate(request));
    }
}
