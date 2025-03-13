package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.OrderDetail;
import com.emotionalcart.order.domain.dto.UserOrder;
import com.emotionalcart.order.domain.entity.OrderItemOption;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.product.ProductService;
import com.emotionalcart.order.infra.product.dto.ProductDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        PageRequest pageRequest = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by(Sort.Order.desc("orderAt")));
        Page<Orders> orderList = orderRepository.findByOrderMemberId(userId, pageRequest);
        List<List<ProductDetail>> productDetailsByOrders =
            orderList.stream().map(order -> order.getOrderItems().stream().map(orderItem -> {
                ProductDetail productDetail = productService.getProductDetail(orderItem.getProductId());
                Map<Long, Long> optionMap = orderItem.getOrderItemOptions().stream()
                    .collect(Collectors.toMap(
                        OrderItemOption::getProductOptionId,
                        OrderItemOption::getProductOptionDetailId,
                        (existing, replacement) -> replacement
                    ));
                productDetail.getProductOptions().forEach(option -> {
                    if (optionMap.containsKey(option.getId())) {
                        option.updateOptionDetail(optionMap.get(option.getId()));
                    }
                });
                return productDetail;
            }).toList()).toList();
        List<UserOrder> userOrders = IntStream.range(0, orderList.getContent().size())
            .mapToObj(i -> UserOrder.from(orderList.getContent().get(i), productDetailsByOrders.get(i)))
            .toList();

        return new PageImpl<>(userOrders, request, orderList.getTotalElements());
    }

    public Boolean validateOrderByMember(Long userId, Long orderId) {
        return orderRepository.findByIdAndOrderMemberId(orderId, userId).isPresent() ? Boolean.TRUE : Boolean.FALSE;

    }

}
