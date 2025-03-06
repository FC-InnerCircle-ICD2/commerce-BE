package com.emotionalcart.order.presentation.admin.controller;

import com.emotionalcart.order.infra.search.dto.AdminSearchResponse;
import com.emotionalcart.order.infra.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/search")
public class AdminSearchController {

    private final SearchService service;

    @GetMapping
    public AdminSearchResponse search(String keyword) {
        return service.search(keyword);
    }

}
