package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.BannerService;
import com.emotionalcart.product.presentation.dto.ReadBanners;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<List<ReadBanners.Response>> readBanners() {
        return ResponseEntity.ok(bannerService.readBanners());
    }
}
