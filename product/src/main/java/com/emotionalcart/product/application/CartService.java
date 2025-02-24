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
import com.emotionalcart.product.presentation.dto.request.DeleteCartItemsRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartResponse;
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

        cart.setItems(cartItems);

        try {
            redisTemplate.opsForValue().set(cartId, cart, 24, TimeUnit.HOURS); // 24시간 만료
        } catch (DataAccessException e) {
            throw new ProductException(ErrorCode.CART_ACCESS_ERROR);
        }

        return cart;
    }

    // 장바구니 아이템 수량 변경
    public ReadCart.Response updateCartItemQuantity(Long userId, Long productId, Long optionDetailId,
            UpdateCartItemQuantityRequest request) {
        String cartId = CART_KEY_PREFIX + userId;
        ReadCart.Response cart = readCart(userId);
        List<ReadCart.CartItem> cartItems = cart.getItems();

        // 장바구니에서 수량 변경 대상 상품이 없는 경우
        boolean itemExists = cartItems.stream()
                .anyMatch(item -> item.getProductId().equals(productId)
                        && item.getOption().getOptionDetail().getId().equals(optionDetailId));
        if (!itemExists) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        // 수량 업데이트 및 선택된 상태 확인
        boolean wasSelected = false;
        for (ReadCart.CartItem item : cartItems) {
            if (item.getProductId().equals(productId)
                    && item.getOption().getOptionDetail().getId().equals(optionDetailId)) {
                wasSelected = item.isSelected(); // 기존에 선택된 상태인지 확인
                item.setOptionDetailQuantity(request.getOptionDetailQuantity());
                item.setPrice(item.getPrice() * item.getOptionDetailQuantity());
            }
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
    public ReadCart.Response selectCartItem(Long userId, Long productId, Long optionDetailId) {
        String cartId = CART_KEY_PREFIX + userId;
        ReadCart.Response cart = readCart(userId);
        List<ReadCart.CartItem> cartItems = cart.getItems();

        // 장바구니에서 선택 대상 상품이 없는 경우
        boolean itemExists = cartItems.stream()
                .anyMatch(item -> item.getProductId().equals(productId)
                        && item.getOption().getOptionDetail().getId().equals(optionDetailId));
        if (!itemExists) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        cartItems.forEach(item -> {
            if (item.getProductId().equals(productId)
                    && item.getOption().getOptionDetail().getId().equals(optionDetailId)) {
                item.setSelected(true);
            }
        });

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

        // 장바구니에서 삭제 대상 상품이 없는 경우
        boolean itemExists = cartItems.stream()
                .anyMatch(item -> request.getItems().stream()
                        .anyMatch(reqItem -> reqItem.getProductId().equals(item.getProductId())
                                && reqItem.getOptionDetailId().equals(item.getOption().getOptionDetail().getId())));
        if (!itemExists) {
            throw new ProductException(ErrorCode.NOT_FOUND_PRODUCT);
        }

        // 삭제 대상 중 선택된 아이템이 있는지 확인
        boolean wasAnySelected = cartItems.stream()
                .anyMatch(item -> item.isSelected() && request.getItems().stream()
                        .anyMatch(reqItem -> reqItem.getProductId().equals(item.getProductId())
                                && reqItem.getOptionDetailId().equals(item.getOption().getOptionDetail().getId())));

        // 아이템 삭제
        List<ReadCart.CartItem> updatedCartItems = cartItems.stream()
                .filter(item -> !request.getItems().stream()
                        .anyMatch(reqItem -> reqItem.getProductId().equals(item.getProductId())
                                && reqItem.getOptionDetailId().equals(item.getOption().getOptionDetail().getId())))
                .collect(Collectors.toList());

        cart.setItems(updatedCartItems);

        // 선택된 아이템이 삭제되었다면 총 가격 재계산
        if (wasAnySelected) {
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
