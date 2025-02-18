package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.application.AdminBannerService;
import com.emotionalcart.adminproduct.presentation.dto.CreateBannerRequest;
import com.emotionalcart.adminproduct.presentation.dto.CreateBannerResponse;
import com.emotionalcart.adminproduct.presentation.dto.ReadBannerDetailResponse;
import com.emotionalcart.adminproduct.presentation.dto.ReadBannersResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/banners")
@RequiredArgsConstructor
public class AdminBannerController implements AdminBannerControllerDocs{
    private final AdminBannerService adminBannerService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateBannerResponse> createBanner(@ModelAttribute @Valid CreateBannerRequest request) {
        return ResponseEntity.ok(adminBannerService.createBanner(request));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ReadBannersResponse>> readBanners() {
        return ResponseEntity.ok(adminBannerService.readBanners());
    }

    @Override
    @GetMapping("/{bannerId}")
    public ResponseEntity<ReadBannerDetailResponse> readBannerDetail(@PathVariable(name = "bannerId") Long bannerId) {
        return ResponseEntity.ok(adminBannerService.readBannerDetail(bannerId));
    }
}
