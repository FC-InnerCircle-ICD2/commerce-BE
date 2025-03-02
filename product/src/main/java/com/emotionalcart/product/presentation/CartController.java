package com.emotionalcart.product.presentation;

import com.emotionalcart.common.jwt.JwtAuthentication;
import com.emotionalcart.product.application.CartService;
import com.emotionalcart.product.presentation.dto.ReadCart;
import com.emotionalcart.product.presentation.dto.request.AddCartItemRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartItemsRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartResponse;
import com.emotionalcart.product.presentation.dto.request.UpdateCartItemQuantityRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController implements CartControllerDocs {

    private final CartService cartService;

    // 장바구니 조회
    @Override
    public ResponseEntity<ReadCart.Response> readCart(@AuthenticationPrincipal JwtAuthentication jwt) {
        return ResponseEntity.ok(cartService.readCart(jwt.id()));
    }

    // 장바구니 아이템 추가
    @Override
    public ResponseEntity<ReadCart.Response> addCart(@AuthenticationPrincipal JwtAuthentication jwt,
            @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addCartItem(jwt.id(), request));
    }

    // 장바구니 아이템 수량 변경
    @Override
    public ResponseEntity<ReadCart.Response> updateCartItemQuantity(@AuthenticationPrincipal JwtAuthentication jwt,
            @RequestBody UpdateCartItemQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(jwt.id(), request));
    }

    // 장바구니 아이템 선택
    @Override
    public ResponseEntity<ReadCart.Response> selectCartItem(@AuthenticationPrincipal JwtAuthentication jwt,
            @PathVariable String itemId) {
        return ResponseEntity.ok(cartService.selectCartItem(jwt.id(), itemId));
    }

    // 장바구니 비우기
    @Override
    public ResponseEntity<DeleteCartResponse> clearCart(@AuthenticationPrincipal JwtAuthentication jwt) {
        return ResponseEntity.ok(cartService.clearCart(jwt.id()));
    }

    // 장바구니 아이템 삭제
    @Override
    public ResponseEntity<ReadCart.Response> deleteCartItems(@AuthenticationPrincipal JwtAuthentication jwt,
            @RequestBody DeleteCartItemsRequest request) {
        return ResponseEntity.ok(cartService.deleteCartItems(jwt.id(), request));
    }

}
