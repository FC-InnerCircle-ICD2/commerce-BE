package com.emotionalcart.adminorder.presentation.controller;

import com.emotionalcart.adminorder.application.AdminOrderService;
import com.emotionalcart.adminorder.presentation.controller.response.AdminOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1")
@RequiredArgsConstructor
public class AdminOrderController implements AdminOrderApiDocs {

    private final AdminOrderService adminOrderService;

    /**
     * 주문 목록 조회
     *
     * @return
     */
    @GetMapping("/orders")
    public ResponseEntity<Page<AdminOrder>> getOrderList(@PageableDefault(sort = "orderAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminOrderService.getOrderList(pageable));
    }

}
