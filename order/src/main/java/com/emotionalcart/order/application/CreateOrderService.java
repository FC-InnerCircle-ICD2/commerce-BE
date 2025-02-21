package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.CardInfo;
import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.entity.OrderItem;
import com.emotionalcart.order.domain.entity.OrderItemHistory;
import com.emotionalcart.order.domain.entity.OrderStatistics;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.domain.repository.OrderItemHistoryRepository;
import com.emotionalcart.order.domain.repository.OrderStatisticsRepository;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import com.emotionalcart.order.infra.advice.exceptions.RedissonLockException;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.payment.PaymentInfo;
import com.emotionalcart.order.infra.payment.PaymentService;
import com.emotionalcart.order.infra.product.ProductService;
import com.emotionalcart.order.infra.product.dto.ProductPrice;
import com.emotionalcart.order.infra.product.dto.ProductPriceRequest;
import com.emotionalcart.order.infra.product.dto.ProductQuantityValidateRequest;
import com.emotionalcart.order.infra.product.dto.ProductStockRequest;
import com.emotionalcart.order.infra.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final OrderStatisticsRepository orderStatisticsRepository;
    private final OrderItemHistoryRepository orderItemHistoryRepository;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final RedissonMultiLockProvider redissonMultiLockProvider;
    private final StockService stockService;

    /**
     * 주문 생성
     *
     * @param createOrder 주문 생성 정보
     * @return 생성된 주문
     */
    @Transactional
    public CreatedOrder createOrder(CreateOrder createOrder) {

        Orders orders = validateAndSaveOrder(createOrder);

        RedissonMultiLock multiLock = redissonMultiLockProvider.getRedissonMultiLock(createOrder);

        try {
            if (multiLock.tryLock(10, 10, TimeUnit.SECONDS)) {

                validateQuantity(createOrder);
                requestOriginalPriceAndValidatePrice(createOrder);
                payment(orders, createOrder.getCardInfo());
                shipment(orders);
                orderHistory(orders);
                updateQuantity(orders);
                orderStatistics(orders.getOrderItems());
            } else {
                throw new RedissonLockException("잠금 획득 실패: 다른 사용자가 처리 중입니다.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RedissonLockException(e.getMessage());
        } finally {
            multiLock.unlock();
        }
        return CreatedOrder.from(orders);
    }

    private void orderHistory(Orders orders) {
        List<OrderItemHistory> orderItemHistoryList =
            orders.getOrderItems().stream().map(OrderItemHistory::createOrderItemHistory).toList();
        orderItemHistoryRepository.saveAll(orderItemHistoryList);
    }

    private void updateQuantity(Orders orders) {
        List<ProductStockRequest> productStockRequests = new ArrayList<>();
        for (OrderItem orderItem : orders.getOrderItems()) {
            ProductStockRequest request = ProductStockRequest.of(orderItem.getProductId(), orderItem.getQuantity());
            orderItem.getOrderItemOptions().forEach(option -> request.addOption(option.getProductOptionId(),
                                                                                option.getProductOptionDetailId()));
            productStockRequests.add(request);
        }
        stockService.deductStockQuantity(productStockRequests);
    }

    /**
     * 상품 수량 검증
     *
     * @param createOrder
     */
    private void validateQuantity(CreateOrder createOrder) {
        List<ProductQuantityValidateRequest> productQuantityValidateRequests = new ArrayList<>();
        for (CreateOrderItem orderItem : createOrder.getOrderItems()) {
            ProductQuantityValidateRequest request = ProductQuantityValidateRequest.of(orderItem.getProductId(), orderItem.getQuantity());
            orderItem.getOrderItemOptions().forEach(option -> request.addOption(option.getProductOptionId(),
                                                                                option.getProductOptionDetailId(),
                                                                                orderItem.getQuantity()));
            productQuantityValidateRequests.add(request);
        }
        stockService.isValidProductQuantity(productQuantityValidateRequests);
    }

    private Orders validateAndSaveOrder(CreateOrder createOrder) {
        createOrder.valid();
        log.info("saveRequest: {}", createOrder);
        Orders orders = Orders.createOrder(createOrder);
        orderRepository.save(orders);
        log.info("created order.id: {}", orders.getId());
        return orders;
    }

    private void requestOriginalPriceAndValidatePrice(CreateOrder createOrder) {
        List<ProductPriceRequest> productPriceRequests = new ArrayList<>();
        for (CreateOrderItem orderItem : createOrder.getOrderItems()) {
            ProductPriceRequest productPriceRequest = ProductPriceRequest.of(orderItem.getProductId());
            orderItem.getOrderItemOptions().forEach(option -> productPriceRequest.addOption(option.getProductOptionId(),
                                                                                            option.getProductOptionDetailId()));
            productPriceRequests.add(productPriceRequest);
        }
        log.info("request product price: {}", productPriceRequests);
        validatePrice(productPriceRequests);
    }

    private void validatePrice(List<ProductPriceRequest> productPriceRequests) {
        List<ProductPrice> productPriceList = productService.getProductPrice(productPriceRequests);
        Map<Long, Double> productPriceMap = productPriceList.stream()
            .collect(Collectors.toMap(ProductPrice::getProductId, ProductPrice::getPrice));
        log.info("response product price: {}", productPriceMap);
        for (ProductPrice productPrice : productPriceList) {
            if (productPriceMap.get(productPrice.getProductId()) != productPrice.getPrice()) {
                throw new InvalidValueRequestException("상품 가격이 변경되었습니다. 새로고침 이후 다시 이용 부탁드립니다.");
            }
        }
    }

    /**
     * 주문 통계 테이블 저장
     *
     * @param orderItems
     */
    private void orderStatistics(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            OrderStatistics orderStatistics =
                orderStatisticsRepository.findByProductIdAndCategoryId(orderItem.getProductId(), orderItem.getCategoryId()).orElse(
                    OrderStatistics.create(orderItem));
            orderStatistics.updateOrderStatistics(orderItem);
            orderStatisticsRepository.save(orderStatistics);
        }
    }

    private void payment(Orders orders, CardInfo cardInfo) {
        log.info("request payment orders.id: {}, cardInfo {}", orders.getId(), cardInfo);
        paymentService.pay(PaymentInfo.create());
        orders.requestPayment();
    }

    private void shipment(Orders orders) {
        log.info("request shipment orders.id: {}", orders.getId());
        // TODO 배송 서비스 호출
        orders.requestShipment();
    }

}
