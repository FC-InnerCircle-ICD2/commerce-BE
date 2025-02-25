package com.emotionalcart.member.presentation;

import com.emotionalcart.core.feature.Member;
import com.emotionalcart.core.feature.enums.MemberRole;
import com.emotionalcart.member.application.AdminMemberService;
import com.emotionalcart.member.application.TokenInfo;
import com.emotionalcart.member.presentation.dto.AdminMemberResponse;
import com.emotionalcart.member.presentation.dto.CreateAdminMemberRequest;
import com.emotionalcart.member.presentation.dto.LoginAdminRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/admin-users")
    public ResponseEntity<Page<AdminMemberResponse>> getAdminUserList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> adminMembers = adminMemberService.getAdminUserList(pageable);
        return ResponseEntity.ok().body(adminMembers.map(AdminMemberResponse::from));
    }

}
