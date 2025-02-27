package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.application.AdminProviderService;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProvidersRequest;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProvidersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/provider")
@RequiredArgsConstructor
public class AdminProviderController implements AdminProviderControllerDocs {
    private  final AdminProviderService adminProviderService;

    @Override
    @GetMapping
    public ResponseEntity<Page<ReadAdminProvidersResponse>> readProviders(ReadAdminProvidersRequest request) {
        return ResponseEntity.ok(adminProviderService.readProviders(request));
    }
}
