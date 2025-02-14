package com.emotionalcart.adminorder.presentation.controller;

import com.emotionalcart.adminorder.presentation.controller.response.AdminOrder;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

public interface AdminOrderApiDocs {

    @Operation(summary = "관리자 주문 목록 조회 API")
    ResponseEntity<Page<AdminOrder>> getOrderList(@PageableDefault Pageable pageable);

}
