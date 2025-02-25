package com.emotionalcart.member.presentation;

import com.emotionalcart.common.jwt.JwtAuthentication;
import com.emotionalcart.core.feature.Member;
import com.emotionalcart.member.application.AdminMemberService;
import com.emotionalcart.member.application.TokenInfo;
import com.emotionalcart.member.presentation.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/members/auth")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @PostMapping
    public ResponseEntity<Boolean> createAdminUser(@RequestBody @Valid CreateAdminMemberRequest request) {
        return ResponseEntity.ok(adminMemberService.createAdminUser(request.mapToCommand()));
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> loginAdminUser(@RequestBody @Valid LoginAdminRequest request, HttpServletResponse response) {
        TokenInfo tokenInfo = adminMemberService.login(request.getEmail(), request.getPassword());
        response.setHeader("Access-Token", tokenInfo.getAccessToken());
        response.setHeader("Refresh-Token", tokenInfo.getRefreshToken());
        return ResponseEntity.ok(true);
    }

    @GetMapping("/admin-members")
    public ResponseEntity<Page<AdminMembersResponse>> getAdminMembers(@AuthenticationPrincipal JwtAuthentication jwt, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> adminMembers = adminMemberService.getAdminMembers(jwt.id(), pageable);
        return ResponseEntity.ok().body(adminMembers.map(AdminMembersResponse::from));
    }

    @GetMapping("/admin-member/{memberId}")
    public ResponseEntity<AdminMemberInfoResponse> getAdminUserInfo(@AuthenticationPrincipal JwtAuthentication jwt, @PathVariable Long memberId) {
        Member member = adminMemberService.getAdminMemberInfo(jwt.id(), memberId);
        return ResponseEntity.ok().body(AdminMemberInfoResponse.from(member));
    }

    @PutMapping("/admin-member/{memberId}")
    public ResponseEntity<AdminMemberInfoResponse> updateAdminUserInfo(@AuthenticationPrincipal JwtAuthentication jwt, @PathVariable Long memberId, @RequestBody @Valid UpdateAdminMemberRequest request) {
        Member member = adminMemberService.updateAdminMemberInfo(jwt.id(), memberId, request.mapToCommand());
        return ResponseEntity.ok().body(AdminMemberInfoResponse.from(member));
    }
}
