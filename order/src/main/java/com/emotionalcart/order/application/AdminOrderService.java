package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.AdminOrder;
import com.emotionalcart.order.domain.repository.AdminOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Page<AdminOrder> getOrderList(Long orderId, Pageable pageable) {
        if (Optional.ofNullable(orderId).isPresent()) {

            return adminOrderRepository.findById(orderId, pageable).map(orders -> AdminOrder.of(orders.getId(),
                                                                                                orders.getPaymentMethod().getMethodName(),
                                                                                                orders.getStatus().getStatusName(),
                                                                                                orders.getOrderAt(),
                                                                                                orders.getTotalPrice()));
        }

        return adminOrderRepository.findAll(pageable).map(orders -> AdminOrder.of(orders.getId(),
                                                                                  orders.getPaymentMethod().getMethodName(),
                                                                                  orders.getStatus().getStatusName(),
                                                                                  orders.getOrderAt(),
                                                                                  orders.getTotalPrice()));

    }

}
