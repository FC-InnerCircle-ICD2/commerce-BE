package com.emotionalcart.order.application.service;

import com.emotionalcart.order.application.CreateOrderService;
import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import com.emotionalcart.order.infra.advice.exceptions.RequiredValueException;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.payment.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private CreateOrderService createOrderService;

    @Test
    @DisplayName("주문 만들기 성공")
    void createOrder_success() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(1L, 1L, "상품명", 1000L, 1);
        createOrder.createNewCardInfo("1234567890123456", "12/24", "123", "ddd");
        createOrder.createDeliveryInfo("이름", "010-1234-5678", "12345", "서울시 강남구", "상세주소", "비고");
        // when
        when(orderRepository.save(any())).thenReturn(Orders.defaultOrder());
        CreatedOrder order = createOrderService.createOrder(createOrder);
        // then
        assertThat(order).isNotNull();
        assertThat(order.getId()).isNotNull();
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
        createOrder.addItem(1L, 1L, "상품명", 1000L, 1);
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
        createOrder.addItem(null, 1L, "상품명", 1000L, 1);
        createOrder.createNewCardInfo("1234567890123456", "12/21", "123", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);

        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOf(RequiredValueException.class)
            .hasMessageContaining("상품을 선택해주세요.");

    }

    @Test
    @DisplayName("주문 만들기 실패 - 상품 옵션 ID 없음")
    void createOrder_fail_product_option_id() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(1L, null, "상품명", 1000L, 1);
        createOrder.createNewCardInfo("1234567890123456", "12/21", "123", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOf(RequiredValueException.class)
            .hasMessageContaining("상품 옵션을 선택해주세요.");
    }

    @Test
    @DisplayName("주문 만들기 실패 - 상품명 없음")
    void createOrder_fail_no_product_name() {
        // given
        CreateOrder createOrder = CreateOrder.builder().paymentMethod(PaymentMethod.CARD).build();
        createOrder.addItem(1L, null, null, 1000L, 1);
        createOrder.createNewCardInfo("1234567890123456", "12/21", "123", "ddd");
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
        createOrder.addItem(1L, null, null, 99L, 1);
        createOrder.createNewCardInfo("1234567890123456", "12/21", "123", "ddd");
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
        createOrder.addItem(1L, null, null, 1000L, 0);
        createOrder.createNewCardInfo("1234567890123456", "12/21", "123", "ddd");
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
        createOrder.addItem(1L, 1L, "상품명", 1000L, 1);
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
        createOrder.addItem(1L, 1L, "상품명", 1000L, 1);
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
        createOrder.addItem(1L, 1L, "상품명", 1000L, 1);
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
        createOrder.addItem(1L, 1L, "상품명", 1000L, 1);
        createOrder.createNewCardInfo("1234567890123456", "12/21", "abc", "ddd");
        createOrder.createDeliveryInfo(null, null, null, null, null, null);
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("CVC는 숫자로만 입력해주세요.");
        // given
        createOrder.createNewCardInfo("1234567890123456", "12/21", "1234", "ddd");
        // when & then
        assertThatThrownBy(() -> createOrderService.createOrder(createOrder))
            .isInstanceOfAny(InvalidValueRequestException.class)
            .hasMessageContaining("CVC는 3자리여야 합니다.");
    }

}