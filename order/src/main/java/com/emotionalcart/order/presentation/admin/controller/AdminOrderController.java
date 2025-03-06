package com.emotionalcart.order.presentation.admin.controller;

import com.emotionalcart.order.application.AdminOrderService;
import com.emotionalcart.order.domain.dto.AdminOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<AdminOrder>> getOrderList(@RequestParam(required = false) Long orderId,
                                                         @PageableDefault(sort = "orderAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminOrderService.getOrderList(orderId, pageable));
    }

}
