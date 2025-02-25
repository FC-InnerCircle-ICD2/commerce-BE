package com.emotionalcart.product.application;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.product.presentation.dto.ReadCart;
import com.emotionalcart.product.presentation.dto.request.AddCartItemRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartItemsRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartResponse;
import com.emotionalcart.product.presentation.dto.request.SelectCartItemRequest;
import com.emotionalcart.product.presentation.dto.request.UpdateCartItemQuantityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        cart.setCartId(cartId);
        cart.setItems(cartItems);
        

        try {
            redisTemplate.opsForValue().set(cartId, cart, 24, TimeUnit.HOURS); // 24시간 만료
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }

        return cart;
    }

    // 장바구니 아이템 수량 변경
    public ReadCart.Response updateCartItemQuantity(Long userId, UpdateCartItemQuantityRequest request) {
        String cartId = CART_KEY_PREFIX + userId;
        ReadCart.Response cart = readCart(userId);
        List<ReadCart.CartItem> cartItems = cart.getItems();

        boolean itemExists = false;
        boolean wasSelected = false;

        for (ReadCart.CartItem item : cartItems) {
            if (item.getProductId().equals(request.getProductId())
                    && item.getOption().getId().equals(request.getOptionId())
                    && item.getOption().getOptionDetail().getId().equals(request.getOptionDetailId())) {
                itemExists = true;
                wasSelected = item.isSelected(); // 기존에 선택된 상태인지 확인
                item.setOptionDetailQuantity(request.getOptionDetailQuantity());
                item.setSubTotalPrice((item.getPrice() + item.getOption().getOptionDetail().getAdditionalPrice())
                        * item.getOptionDetailQuantity());
            }
        }

        if (!itemExists) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        // 기존에 선택된 상태였다면 총 가격 재계산
        if (wasSelected) {
            int total = cart.calculateTotal();
            cart.setTotalPrice(total);
        }

        try {
            redisTemplate.opsForValue().set(cartId, cart, 24, TimeUnit.HOURS); // 24시간 만료
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }

        return cart;
    }

    // 장바구니 내 아이템 선택
    public ReadCart.Response selectCartItem(Long userId, SelectCartItemRequest request) {
        String cartId = CART_KEY_PREFIX + userId;
        ReadCart.Response cart = readCart(userId);
        List<ReadCart.CartItem> cartItems = cart.getItems();

        boolean itemExists = false;

        for (ReadCart.CartItem item : cartItems) {
            if (item.getProductId().equals(request.getProductId())
                    && item.getOption().getId().equals(request.getOptionId())
                    && item.getOption().getOptionDetail().getId().equals(request.getOptionDetailId())) {
                itemExists = true;
                item.setSelected(true);
            }
        }

        // 장바구니에서 선택 대상 상품이 없는 경우
        if (!itemExists) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        cart.setItems(cartItems);

        // 장바구니 총 상품가격 재계산
        int total = cart.calculateTotal();
        cart.setTotalPrice(total);

        try {
            redisTemplate.opsForValue().set(cartId, cart, 24, TimeUnit.HOURS); // 24시간 만료
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }

        return cart;
    }

    // 장바구니 아이템 삭제
    public ReadCart.Response deleteCartItems(Long userId, DeleteCartItemsRequest request) {
        String cartId = CART_KEY_PREFIX + userId;
        ReadCart.Response cart = readCart(userId);
        List<ReadCart.CartItem> cartItems = cart.getItems();

        // 삭제 대상 중 선택된 아이템이 있는지 확인 및 아이템 삭제
        AtomicBoolean wasAnySelected = new AtomicBoolean(false);
        List<ReadCart.CartItem> updatedCartItems = cartItems.stream()
                .filter(item -> {
                    boolean isToDelete = request.getItems().stream()
                            .anyMatch(reqItem -> reqItem.getProductId().equals(item.getProductId())
                                    && reqItem.getOptionId().equals(item.getOption().getId())
                                    && reqItem.getOptionDetailId().equals(item.getOption().getOptionDetail().getId()));
                    if (isToDelete && item.isSelected()) {
                        wasAnySelected.set(true); // 선택된 아이템이 삭제 대상인 경우
                    }
                    return !isToDelete; // 삭제 대상이 아닌 경우만 유지
                })
                .collect(Collectors.toList());

        cart.setItems(updatedCartItems);

        // 선택된 아이템이 삭제되었다면 총 가격 재계산
        if (wasAnySelected.get()) {
            int total = cart.calculateTotal();
            cart.setTotalPrice(total);
        }

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
}
