package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.dto.CreateOrderItemOption;
import com.emotionalcart.order.domain.entity.OrderStatistics;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.order.OrderStatisticsRepository;
import com.emotionalcart.order.infra.payment.PaymentService;
import com.emotionalcart.order.infra.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.RedissonMultiLock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatisticsServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ProductService productService;

    @Mock
    private RedissonMultiLockProvider redissonMultiLockProvider;

    @Mock
    private OrderStatisticsRepository orderStatisticsRepository;

    @InjectMocks
    private CreateOrderService createOrderService;

    @Test
    @DisplayName("주문 만들기 성공 시 주문 통계 저장 테스트")
    void createOrder_success() throws Exception {
        // Mock 데이터 설정
        lenient().when(orderStatisticsRepository.findByProductIdAndCategoryId(anyLong(), anyLong()))
            .thenReturn(Optional.of(OrderStatistics.create(CreateOrderItem.builder().categoryId(2L).productId(2L).price(1000).quantity(
                1).build())));
        createOrder();

        Optional<OrderStatistics> orderStatistics = orderStatisticsRepository.findByProductIdAndCategoryId(2L, 2L);
        assertThat(orderStatistics).isPresent();
        assertThat(orderStatistics.get().getTotalOrder()).isEqualTo(0L);
        assertThat(orderStatistics.get().getTotalQuantitySold()).isEqualTo(0L);
    }

    private void createOrder() throws Exception {
        // given
        Orders mockOrder = mock(Orders.class);
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .categoryId(1L)
                                .orderItemOptions(List.of(CreateOrderItemOption.builder().productOptionId(1L).productOptionDetailId(1L).build()))
                                .price(1000L).quantity(1).build());
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "123", "ddd");
        createOrder.createDeliveryInfo("이름", "010-1234-5678", "12345", "서울시 강남구", "상세주소", "비고");
        when(orderRepository.save(any())).thenReturn(mockOrder);

        // when
        when(orderRepository.save(any())).thenReturn(Orders.defaultOrder());
        // RedissonMultiLock Mocking
        RedissonMultiLock multiLock = mock(RedissonMultiLock.class);
        when(redissonMultiLockProvider.getRedissonMultiLock(createOrder)).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), eq(TimeUnit.SECONDS))).thenReturn(true);
        createOrderService.createOrder(createOrder);
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