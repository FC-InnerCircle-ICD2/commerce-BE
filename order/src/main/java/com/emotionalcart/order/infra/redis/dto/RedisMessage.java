package com.emotionalcart.order.infra.redis.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisMessage<T> {

    private T message; // 전송할 메세지 내용
    private String sender; // 메세지 발신자
    private String roomId; // 메세지 방 번호 || 타겟 Channel

    public RedisMessage(String sender, T message) {
        this.sender = sender;
        this.message = message;
    }

    public static <T> RedisMessage convert(String sender, T message) {
        return new RedisMessage<>(sender, message);
    }

}
