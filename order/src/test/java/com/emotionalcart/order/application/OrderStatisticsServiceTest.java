package com.emotionalcart.order.application.service;

import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.entity.OrderStatistics;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.order.OrderStatisticsRepository;
import com.emotionalcart.order.infra.payment.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderStatisticsServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderStatisticsRepository orderStatisticsRepository;

    @InjectMocks
    private CreateOrderService createOrderService;

    @Test
    @DisplayName("주문 만들기 성공 시 주문 통계 저장 테스트")
    void createOrder_success() {
        // Mock 데이터 설정
        when(orderStatisticsRepository.findByProductIdAndCategoryId(2L, 2L))
            .thenReturn(java.util.Optional.of(OrderStatistics.create(CreateOrderItem.builder().categoryId(2L).productId(2L).price(1000).quantity(
                1).build())));
        createOrder();
        OrderStatistics orderStatistics = orderStatisticsRepository.findByProductIdAndCategoryId(2L, 2L).orElseThrow();
        assertEquals(1L, orderStatistics.getTotalOrder());
        assertEquals(1L, orderStatistics.getTotalQuantitySold());
    }

    private void createOrder() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(2L, 2L, 2L, "상품명", 1000L, 1);
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "123", "ddd");
        createOrder.createDeliveryInfo("이름", "010-1234-5678", "12345", "서울시 강남구", "상세주소", "비고");
        // when
        when(orderRepository.save(any())).thenReturn(Orders.defaultOrder());
        CreatedOrder order = createOrderService.createOrder(createOrder);
    }

    private String getValidExpirationDate() {
        LocalDate now = LocalDate.now();
        int month = now.getMonth().getValue() + 1;
        String monthStr = month < 10 ? "0" + month : String.valueOf(month);
        int year = now.getYear();
        String yearStr = String.valueOf(year).substring(2);
        return monthStr.concat("/").concat(yearStr);
    }

}