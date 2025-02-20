package com.emotionalcart.member.presentation;

import com.emotionalcart.member.application.AdminMemberService;
import com.emotionalcart.member.application.TokenInfo;
import com.emotionalcart.member.presentation.dto.CreateAdminMemberRequest;
import com.emotionalcart.member.presentation.dto.LoginAdminRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
