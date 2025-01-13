package com.emotionalcart.order.presentation.client.converter;

import feign.QueryMapEncoder;
import feign.codec.EncodeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomQueryMapEncoder implements QueryMapEncoder {

    @Override
    public Map<String, Object> encode(Object object) {
        Map<String, Object> queryMap = new HashMap<>();

        if (object instanceof List<?>) {
            List<?> list = (List<?>)object;

            for (Object item : list) {
                if (item instanceof Map<?, ?>) {
                    Map<?, ?> map = (Map<?, ?>)item;

                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        String key = entry.getKey().toString();
                        Object value = entry.getValue();

                        // 키가 이미 존재하면 리스트 형태로 추가
                        queryMap.merge(key, value, (existing, newValue) -> {
                            if (existing instanceof List) {
                                ((List<Object>)existing).add(newValue);
                                return existing;
                            } else {
                                return List.of(existing, newValue);
                            }
                        });
                    }
                } else {
                    throw new EncodeException("List contains unsupported type: " + item.getClass().getName());
                }
            }
        } else {
            throw new EncodeException("Unsupported type for QueryMapEncoder: " + object.getClass().getName());
        }

        return queryMap;
    }

}