package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.application.AdminBannerService;
import com.emotionalcart.adminproduct.presentation.dto.CreateBannerRequest;
import com.emotionalcart.adminproduct.presentation.dto.CreateBannerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/banners")
@RequiredArgsConstructor
public class AdminBannerController implements AdminBannerControllerDocs{
    private final AdminBannerService adminBannerService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateBannerResponse> createBanner(@ModelAttribute @Valid CreateBannerRequest request) {
        return ResponseEntity.ok(adminBannerService.createBanner(request));
    }
}
