package com.emotionalcart.adminorder.application;

import com.emotionalcart.adminorder.domain.repository.AdminOrderRepository;
import com.emotionalcart.adminorder.presentation.controller.response.AdminOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final AdminOrderRepository adminOrderRepository;

    /**
     * 주문 목록 조회
     * 현재는 시스템 관리자 기준으로 개발
     * 추후에는 업체도 고려해서 개발 필요
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Page<AdminOrder> getOrderList(Pageable pageable) {
        return adminOrderRepository.findAll(pageable).map(AdminOrder::fromEntity);
    }

}
