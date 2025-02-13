package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.application.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final AdminProductService adminProductService;

}
