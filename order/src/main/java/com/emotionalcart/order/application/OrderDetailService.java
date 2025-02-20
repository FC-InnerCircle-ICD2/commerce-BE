package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.OrderDetail;
import com.emotionalcart.order.domain.dto.UserOrder;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.product.ProductService;
import com.emotionalcart.order.infra.product.dto.ProductDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public OrderDetail getOrderDetail(Long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new InvalidValueRequestException("일치하는 주문번호를 찾을 수 없습니다."));
        log.error("일치하는 주문번호를 찾을 수 없습니다 : {}", orderId);
        return OrderDetail.from(orders);
    }

    /**
     * 사용자 별 주문 목록 조회
     *
     * @param userId 사용자 아이디
     * @return
     */
    public Page<UserOrder> getOrderListByUserId(Long userId, Pageable request) {
        Page<Orders> orderList = orderRepository.findByUserId(userId, request);
        List<List<ProductDetail>> productDetailsByOrders =
            orderList.stream().map(order -> order.getOrderItems().stream().map(orderItem -> productService.getProductDetail(orderItem.getProductId())).toList()).toList();

        List<UserOrder> userOrders = IntStream.range(0, orderList.getContent().size())
            .mapToObj(i -> UserOrder.from(orderList.getContent().get(i), productDetailsByOrders.get(i)))
            .toList();

        // 최종적으로 Page<UserOrder> 반환
        return new PageImpl<>(userOrders, request, orderList.getTotalElements());
    }

}
