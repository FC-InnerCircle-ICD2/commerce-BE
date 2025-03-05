package com.emotionalcart.order.infra.order.producer;

import com.emotionalcart.order.infra.order.OrderSaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final String QUEUE_NAME = "order-queue";

    public OrderEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void sendSalesEvent(OrderSaveRequest orderSaveRequest) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, orderSaveRequest);
        log.info("Sent: {}", orderSaveRequest);
    }

}