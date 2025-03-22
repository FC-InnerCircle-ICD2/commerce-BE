package com.emotionalcart.shipment.infra.redis;

import com.emotionalcart.shipment.infra.redis.component.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisShipmentScheduler implements InitializingBean {

    private String streamKey;
    private String consumerGroupName;
    private String consumerName;
    private final RedisOperator redisOperator;
    private final ShipmentConsumerService shipmentConsumerService;

    @Scheduled(fixedRate = 10000)
    public void processPendingMessage() {
        PendingMessages pendingMessages = this.redisOperator
            .findStreamPendingMessages(streamKey, consumerGroupName, consumerName);

        for (PendingMessage pendingMessage : pendingMessages) {
            this.redisOperator.claimStream(pendingMessage, consumerName);
            try {
                MapRecord<String, Object, Object> messageToProcess = this.redisOperator
                    .findStreamMessageById(this.streamKey, pendingMessage.getIdAsString());
                if (messageToProcess == null) {
                    log.info("존재하지 않는 메시지입니다.");
                }
                String errorCount = (String)this.redisOperator
                    .getRedisValue("errorCount", pendingMessage.getIdAsString());

                if (Integer.parseInt(errorCount) >= 4) {
                    log.info("재 처리 최대 횟수 초과 하였습니다.");

                } else if (pendingMessage.getTotalDeliveryCount() >= 2) {
                    log.info("최대 delivery 횟수 초과 하였습니다.");
                } else {
                    this.shipmentConsumerService.consumeShipmentEvents();
                }
                this.redisOperator.ackStream(consumerGroupName, messageToProcess);
            } catch (Exception e) {
                log.info("error : {}", e.getMessage());
                this.redisOperator.increaseRedisValue("errorCount", pendingMessage.getIdAsString());
            }
        }
    }

    /**
     * 빈 등록 시점에 값 정의
     */
    @Override
    public void afterPropertiesSet() {
        this.streamKey = "shipment:request:stream";
        this.consumerGroupName = "shipment-consumers";
        this.consumerName = "shipment-processor-1";
    }

}