package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.application.AdminProviderService;
import com.emotionalcart.adminproduct.presentation.dto.CreateProviderRequest;
import com.emotionalcart.adminproduct.presentation.dto.CreateProviderResponse;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProvidersRequest;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProvidersResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/v1/provider")
@RequiredArgsConstructor
public class AdminProviderController implements AdminProviderControllerDocs {
    private  final AdminProviderService adminProviderService;

    @Override
    @PostMapping
    public ResponseEntity<CreateProviderResponse> createProvider(
        @Valid @ModelAttribute CreateProviderRequest request) {
        return ResponseEntity.ok(adminProviderService.createProvider(request));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<ReadAdminProvidersResponse>> readProviders(ReadAdminProvidersRequest request) {
        return ResponseEntity.ok(adminProviderService.readProviders(request));
    }
}
