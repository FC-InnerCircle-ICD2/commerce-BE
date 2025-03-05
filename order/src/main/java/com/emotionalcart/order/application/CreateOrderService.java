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
import com.emotionalcart.order.infra.order.OrderSaveRequest;
import com.emotionalcart.order.infra.order.producer.OrderEventProducer;
import com.emotionalcart.order.infra.payment.PaymentInfo;
import com.emotionalcart.order.infra.payment.PaymentService;
import com.emotionalcart.order.infra.product.ProductService;
import com.emotionalcart.order.infra.product.dto.ProductPrice;
import com.emotionalcart.order.infra.product.dto.ProductPriceRequest;
import com.emotionalcart.order.infra.product.dto.ProductQuantityValidateRequest;
import com.emotionalcart.order.infra.product.dto.ProductStockRequest;
import com.emotionalcart.order.infra.shipment.ShipmentService;
import com.emotionalcart.order.infra.shipment.dto.OrderShipmentRequest;
import com.emotionalcart.order.infra.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final ShipmentService shipmentService;
    private final StockService stockService;
    private final OrderEventProducer salesEventProducer;

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
                log.error("now Time :: {}", LocalDateTime.now());
                validateQuantity(createOrder);
                List<ProductPrice> productPriceList = requestOriginalPriceAndValidatePrice(createOrder);
                payment(orders, createOrder.getCardInfo());
                shipment(orders, productPriceList);
                orderHistory(orders);
                updateQuantity(orders);
                orderStatistics(orders.getOrderItems());
                salesEventProducer.sendSalesEvent(OrderSaveRequest.from(orders));
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

    private List<ProductPrice> requestOriginalPriceAndValidatePrice(CreateOrder createOrder) {
        List<ProductPriceRequest> productPriceRequests = new ArrayList<>();
        for (CreateOrderItem orderItem : createOrder.getOrderItems()) {
            ProductPriceRequest productPriceRequest = ProductPriceRequest.of(orderItem.getProductId(), orderItem.getPrice());
            orderItem.getOrderItemOptions().forEach(option -> productPriceRequest.addOption(option.getProductOptionId(),
                                                                                            option.getProductOptionDetailId(),
                                                                                            option.getAdditionalPrice()));
            productPriceRequests.add(productPriceRequest);
        }
        log.info("request product price: {}", productPriceRequests);
        return validatePrice(productPriceRequests);
    }

    private List<ProductPrice> validatePrice(List<ProductPriceRequest> productPriceRequests) {
        List<ProductPrice> productPriceList = productService.getProductPrice(productPriceRequests);
        Map<Long, Double> productPriceMap = productPriceList.stream()
            .collect(Collectors.toMap(
                ProductPrice::getProductId,
                ProductPrice::getTotalPrice,
                Double::sum
            ));
        log.info("response product price: {}", productPriceMap);
        Map<Long, Double> productPriceByRequest = getProductPriceByRequest(productPriceRequests);
        for (ProductPrice productPrice : productPriceList) {
            if (!Objects.equals(productPriceMap.get(productPrice.getProductId()), productPriceByRequest.get(productPrice.getProductId()))) {
                log.error("product price : {}, request price : {}",
                          productPriceMap.get(productPrice.getProductId()),
                          productPriceByRequest.get(productPrice));
                throw new InvalidValueRequestException("상품 가격이 변경되었습니다. 새로고침 이후 다시 이용 부탁드립니다.");
            }
        }
        return productPriceList;
    }

    private Map<Long, Double> getProductPriceByRequest(List<ProductPriceRequest> productPriceRequests) {
        return productPriceRequests.stream()
            .collect(Collectors.toMap(
                ProductPriceRequest::getProductId,
                request -> request.getProductOptions().stream()
                    .mapToDouble(ProductPriceRequest.ProductOption::getAdditionalPrice)  // 클래스명 수정
                    .sum() + request.getPrice(),
                Double::sum
            ));
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

    private void shipment(Orders orders, List<ProductPrice> productPriceList) {
        log.info("request shipment orders.id: {}", orders.getId());
        OrderShipmentRequest orderShipmentRequest = OrderShipmentRequest.of(orders.getId(), orders.getOrderRecipient(), productPriceList);
        if (Boolean.TRUE.equals(shipmentService.createShipment(orderShipmentRequest))) {
            return;
        }
        log.error("request fail shipment orders.id: {}", orders.getId());
        orders.failRequest();
    }

}
