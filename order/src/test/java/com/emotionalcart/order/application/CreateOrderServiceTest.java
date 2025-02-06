package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.dto.CreateOrderItemOption;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import com.emotionalcart.order.infra.advice.exceptions.RequiredValueException;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.payment.PaymentService;
import com.emotionalcart.order.infra.product.ProductService;
import com.emotionalcart.order.infra.product.dto.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ProductService productService;

    @Mock
    private RedissonMultiLockProvider redissonMultiLockProvider;

    @InjectMocks
    private CreateOrderService createOrderService;

    @Mock
    private RLock mockLock;

    @Test
    @DisplayName("주문 만들기 성공")
    void createOrder_success() throws InterruptedException {
        // given
        Orders mockOrder = mock(Orders.class);

        CreateOrder createOrder = CreateOrder.builder()
            .paymentMethod(PaymentMethod.CARD)
            .build();

        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .categoryId(1L)
                                .orderItemOptions(List.of(CreateOrderItemOption.builder().productOptionId(1L).productOptionDetailId(1L).build()))
                                .price(1000L).quantity(1).build());
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "123", "ddd");
        createOrder.createDeliveryInfo("이름", "010-1234-5678", "12345", "서울시 강남구", "상세주소", "비고");

        when(orderRepository.save(any())).thenReturn(mockOrder);

        ProductPrice productPrice = ProductPrice.builder().productId(1L).price(1000L).build();
        when(productService.getProductPrice(any())).thenReturn(List.of(productPrice));
        // RedissonMultiLock Mocking
        RedissonMultiLock multiLock = mock(RedissonMultiLock.class);
        when(redissonMultiLockProvider.getRedissonMultiLock(createOrder)).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), eq(TimeUnit.SECONDS))).thenReturn(true);
        doNothing().when(multiLock).unlock();

        // when
        CreatedOrder order = createOrderService.createOrder(createOrder);
        assertThat(order.getPaymentMethodName()).isEqualTo(PaymentMethod.CARD.getMethodName());
        // then

        verify(multiLock).tryLock(10, 10, TimeUnit.SECONDS); // MultiLock tryLock 호출 검증
        verify(multiLock).unlock(); // unlock 호출 검증
        verify(orderRepository).save(any(Orders.class)); // save 호출 검증
    }

    @Test
    @DisplayName("주문 만들기 실패 - 상품 없음")
    void createOrder_fail_no_items() {
        // given
        CreateOrder createOrder = CreateOrder.builder().build();
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOf(RequiredValueException.class)
            .hasMessageContaining("주문 상품을 선택해주세요.");
    }

    @Test
    @DisplayName("주문 만들기 실패 - 결제 수단 없음")
    void createOrder_fail_no_payment_method() {
        // given
        CreateOrder createOrder = CreateOrder.builder().build();
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOf(RequiredValueException.class)
            .hasMessageContaining("결제 수단을 선택해주세요.");
    }

    @Test
    @DisplayName("주문 만들기 실패 - 결제수단 카드 이외의 것 선택")
    void createOrder_fail_invalid_payment_method() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.TOSS).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .price(1000L).quantity(1).build());
        createOrder.createDeliveryInfo(null, null, null, null, null, null);

        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("결제 수단은 카드만 가능합니다.");

    }

    @Test
    @DisplayName("주문 만들기 실패 - 상품 ID 없음")
    void createOrder_fail_no_product_id() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productName("상품명")
                                .price(1000L).quantity(1).build());
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "123", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);

        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOf(RequiredValueException.class)
            .hasMessageContaining("상품을 선택해주세요.");

    }

    @Test
    @DisplayName("주문 만들기 실패 - 상품명 없음")
    void createOrder_fail_no_product_name() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .price(1000L).quantity(1).build());
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "123", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOf(RequiredValueException.class)
            .hasMessageContaining("상품명을 입력해주세요.");
    }

    @Test
    @DisplayName("주문 만들기 실패 - 금액 미달")
    void createOrder_fail_no_price_less_then_100() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .price(99L).quantity(1).build());
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "123", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(RequiredValueException.class)
            .hasMessageContaining("상품 금액은 100원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문 만들기 실패 - 수량 없음")
    void createOrder_fail_no_quantity_less_then_1() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .price(1000L).build());
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "123", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOf(RequiredValueException.class)
            .hasMessageContaining("수량은 1개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문 만들기 실패 - 결제 수단 카드인데 카드 정보 없음")
    void createOrder_fail_no_card_payment() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .price(1000L).quantity(1).build());
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("카드 정보를 입력해주세요.");
    }

    @Test
    @DisplayName("주문 만들기 실패 - 카드 정보 유효성 체크")
    void createOrder_fail_no_card_payment_validate_card_info() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .price(1000L).quantity(1).build());
        createOrder.createNewCardInfo(null, null, null, null);
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOf(RequiredValueException.class)
            .satisfies(e -> {
                assertThat(e.getMessage()).contains("카드 번호를 입력해주세요.");
                assertThat(e.getMessage()).contains("만료일을 입력해주세요.");
                assertThat(e.getMessage()).contains("CVC를 입력해주세요.");
                assertThat(e.getMessage()).contains("카드 소유자 이름을 입력해주세요.");
            });
    }

    @Test
    @DisplayName("주문 만들기 실패 - 카드 정보 유효성 체크(16자리 미만)")
    void createOrder_fail_no_card_payment_validate_card_info_card_no_shorter_then_16() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .price(1000L).quantity(1).build());
        createOrder.createNewCardInfo("12345678901234", "1234", "123", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("카드 번호는 16자리여야 합니다.");
        // given
        createOrder.createNewCardInfo("abcddkdkdkd", "1234", "123", "ddd");
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("카드 번호는 숫자나 하이픈(-)으로만 입력해주세요.");
        // given
        createOrder.createNewCardInfo("-----", "1234", "123", "ddd");
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("카드 번호를 올바르게 입력해 주세요.");
    }

    @Test
    @DisplayName("주문 만들기 실패 - 카드 정보 유효성 체크 만료일 숫자 아닌 문자열 입력 및 3자리")
    void createOrder_fail_cvc_not_numeric_and_len_is_3() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(CreateOrderItem.builder()
                                .productId(1L)
                                .productName("상품명")
                                .price(1000L).quantity(1).build());
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "abc", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("CVC는 숫자로만 입력해주세요.");
        // given
        createOrder.createNewCardInfo("1234567890123456", getValidExpirationDate(), "1234", "ddd");
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("CVC는 3자리여야 합니다.");
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