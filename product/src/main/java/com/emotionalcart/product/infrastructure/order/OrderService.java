package com.emotionalcart.product.infrastructure.order;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.product.infrastructure.order.http.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderFeignClient orderFeignClient;

    /**
     * 사용자의 구매 이력 확인
     * @param orderId
     */
    public void validate(Long orderId) {
        Boolean isValid = orderFeignClient.validateOrder(orderId).getBody();
        if (Boolean.FALSE.equals(isValid)) {
            throw new ProductException(ErrorCode.NOT_FOUND_ORDER);
        }
    }

}
