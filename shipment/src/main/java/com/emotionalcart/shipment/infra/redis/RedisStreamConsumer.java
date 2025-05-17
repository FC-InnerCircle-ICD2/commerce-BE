package com.emotionalcart.shipment.infra.redis;

import com.emotionalcart.shipment.application.service.OrderShipmentService;
import com.emotionalcart.shipment.infra.redis.component.RedisOperator;
import com.emotionalcart.shipment.presentation.controller.request.OrderShipmentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStreamConsumer implements StreamListener<String, MapRecord<String, Object, Object>>, InitializingBean, DisposableBean {

    private StreamMessageListenerContainer<String, MapRecord<String, Object, Object>> listenerContainer;
    private Subscription subscription;
    private final OrderShipmentService orderShipmentService;
    private final ObjectMapper objectMapper;

    private String streamKey;
    private String consumerGroupName;
    private String consumerName;

    private final RedisOperator redisOperator;

    @Override
    public void onMessage(MapRecord<String, Object, Object> message) {
        log.info("receive message");
        try {
            OrderShipmentRequest orderShipmentRequest =
                objectMapper.readValue((String)message.getValue().get("shipmentOrderRequest"), OrderShipmentRequest.class);
            orderShipmentService.createShipment(orderShipmentRequest.mapToDomain());
            // 이후, ack stream
            this.redisOperator.ackStream(streamKey, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 빈종료시 종료
     */
    @Override
    public void destroy() {
        if (this.subscription != null) {
            this.subscription.cancel();
        }
        if (this.listenerContainer != null) {
            this.listenerContainer.stop();
        }
    }

    /**
     * 빈생성시 listenerContainer 설정
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.streamKey = "shipment:request:stream";
        this.consumerGroupName = "shipment-consumers";
        this.consumerName = "shipment-processor-1";

        this.redisOperator.createStreamConsumerGroup(streamKey, consumerGroupName);

        this.listenerContainer = this.redisOperator.createStreamMessageListenerContainer();

        this.subscription = this.listenerContainer.receive(
            Consumer.from(this.consumerGroupName, consumerName),
            StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
            this
        );

        this.subscription.await(Duration.ofSeconds(2));

        this.listenerContainer.start();
    }

}