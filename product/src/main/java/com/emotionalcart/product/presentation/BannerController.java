package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.BannerService;
import com.emotionalcart.product.presentation.dto.ReadBanners;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/banners")
@RequiredArgsConstructor
public class BannerController implements BannerControllerDocs {
    private final BannerService bannerService;

    @Override
    public ResponseEntity<List<ReadBanners.Response>> readBanners() {
        return ResponseEntity.ok(bannerService.readBanners());
    }
}
