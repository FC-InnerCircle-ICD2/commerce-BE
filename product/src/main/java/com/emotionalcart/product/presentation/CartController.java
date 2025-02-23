package com.emotionalcart.product.presentation;

import com.emotionalcart.product.application.CartService;
import com.emotionalcart.product.presentation.dto.ReadCart;
import com.emotionalcart.product.presentation.dto.request.AddCartItemRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartResponse;
import com.emotionalcart.product.presentation.dto.request.UpdateCartItemQuantityRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ReadCart.Response> readCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.readCart(userId));
    }

    // 장바구니 아이템 추가
    @PostMapping("/{userId}")
    public ResponseEntity<ReadCart.Response> addCart(@PathVariable Long userId,
            @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addCartItem(userId, request));
    }

    // 장바구니 아이템 수량 변경
    @PutMapping("/{userId}/{productId}")
    public ResponseEntity<ReadCart.Response> updateCartItemQuantity(@PathVariable Long userId,
            @PathVariable Long productId, @RequestBody UpdateCartItemQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(userId, productId, request));
    }

    // 장바구니 내 아이템 선택

    // 장바구니 일부 아이템 삭제
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<ReadCart.Response> deleteCartItem(@PathVariable Long userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.deleteCartItem(userId, productId));
    }

    // 장바구니 비우기
    @DeleteMapping("/{userId}")
    public ResponseEntity<DeleteCartResponse> clearCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

}
