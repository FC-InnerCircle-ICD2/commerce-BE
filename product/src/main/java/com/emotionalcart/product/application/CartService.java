package com.emotionalcart.product.application;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.product.presentation.dto.ReadCart;
import com.emotionalcart.product.presentation.dto.request.AddCartItemRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartResponse;
import com.emotionalcart.product.presentation.dto.request.UpdateCartItemQuantityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CART_KEY_PREFIX = "cart:";

    // 장바구니 조회
    public ReadCart.Response readCart(Long userId) {
        String cartId = CART_KEY_PREFIX + userId;
        try {
            Object cart = redisTemplate.opsForValue().get(cartId);
            ReadCart.Response cartResponse = new ReadCart.Response();
            if (cart != null) {
                cartResponse = objectMapper.convertValue(cart, ReadCart.Response.class);
                cartResponse.setCartId(cartId);
            }
            return cartResponse;
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }
    }

    // 장바구니 추가
    public ReadCart.Response addCartItem(Long userId, AddCartItemRequest request) {
        String cartId = CART_KEY_PREFIX + userId;

        ReadCart.Response cart = readCart(userId);
        List<ReadCart.CartItem> cartItems = cart.getItems();
        cartItems.add(ReadCart.CartItem.from(request));

        cart.setItems(cartItems);

        // 장바구니 집계
        Pair<Integer, Integer> total = calculateTotal(cartItems);
        cart.setTotalQuantity(total.getLeft());
        cart.setTotalPrice(total.getRight());

        try {
            redisTemplate.opsForValue().set(cartId, cart, 24, TimeUnit.HOURS); // 24시간 만료
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }

        return cart;
    }

    // 장바구니 아이템 수량 변경
    public ReadCart.Response updateCartItemQuantity(Long userId, Long productId,
            UpdateCartItemQuantityRequest request) {
        String cartId = CART_KEY_PREFIX + userId;
        ReadCart.Response cart = readCart(userId);
        List<ReadCart.CartItem> cartItems = cart.getItems();

        // 장바구니에서 수량 변경 대상 상품이 없는 경우
        boolean itemExists = cartItems.stream()
                .anyMatch(item -> item.getProductId().equals(productId));
        if (!itemExists) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        // 수량 업데이트
        cartItems.forEach(item -> {
            if (item.getProductId().equals(productId)) {
                item.setDetailOptionQuantity(request.getDetailOptionQuantity());
                item.setProductPrice(item.getProductPrice() * item.getDetailOptionQuantity());
            }
        });

        // 장바구니 집계
        Pair<Integer, Integer> total = calculateTotal(cartItems);
        cart.setTotalQuantity(total.getLeft());
        cart.setTotalPrice(total.getRight());

        try {
            redisTemplate.opsForValue().set(cartId, cart, 24, TimeUnit.HOURS); // 24시간 만료
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }

        return cart;
    }

    // 장바구니 일부 삭제
    public ReadCart.Response deleteCartItem(Long userId, Long productId) {
        String cartId = CART_KEY_PREFIX + userId;
        ReadCart.Response cart = readCart(userId);
        List<ReadCart.CartItem> cartItems = cart.getItems();

        // 장바구니에서 삭제 대상 상품이 없는 경우
        boolean itemExists = cartItems.stream()
                .anyMatch(item -> item.getProductId().equals(productId));
        if (!itemExists) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        List<ReadCart.CartItem> updatedCartItems = cartItems.stream()
                .filter(item -> !item.getProductId().equals(productId))
                .collect(Collectors.toList());

        cart.setItems(updatedCartItems);

        // 장바구니 집계
        Pair<Integer, Integer> total = calculateTotal(updatedCartItems);
        cart.setTotalQuantity(total.getLeft());
        cart.setTotalPrice(total.getRight());

        try {
            redisTemplate.opsForValue().set(cartId, cart, 24, TimeUnit.HOURS); // 24시간 만료
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }

        return cart;
    }

    // 장바구니 비우기
    public DeleteCartResponse clearCart(Long userId) {
        String cartId = CART_KEY_PREFIX + userId;

        ReadCart.Response cart = readCart(userId);

        if (cart == null) {
            throw new ProductException(ErrorCode.CART_NOT_FOUND);
        }

        try {
            redisTemplate.delete(cartId);
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }

        return new DeleteCartResponse(cartId);
    }

    private Pair<Integer, Integer> calculateTotal(List<ReadCart.CartItem> cartItems) {
        int totalQuantity = cartItems.stream()
                .mapToInt(item -> item.getDetailOptionQuantity())
                .sum();
        int totalPrice = cartItems.stream()
                .mapToInt(item -> item.getProductPrice() * item.getDetailOptionQuantity())
                .sum();
        return Pair.of(totalQuantity, totalPrice);
    }
}
