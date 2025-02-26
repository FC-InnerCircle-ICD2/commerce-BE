package com.emotionalcart.product.presentation;

import com.emotionalcart.product.presentation.dto.ReadCart;
import com.emotionalcart.product.presentation.dto.request.AddCartItemRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartItemsRequest;
import com.emotionalcart.product.presentation.dto.request.DeleteCartResponse;
import com.emotionalcart.product.presentation.dto.request.SelectCartItemRequest;
import com.emotionalcart.product.presentation.dto.request.UpdateCartItemQuantityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.emotionalcart.common.jwt.JwtAuthentication;

@Tag(name = "장바구니 API", description = "장바구니 관련 API")
public interface CartControllerDocs {

        @GetMapping
        @Operation(summary = "장바구니 조회", description = "사용자의 장바구니를 조회합니다.")
        ResponseEntity<ReadCart.Response> readCart(@AuthenticationPrincipal JwtAuthentication jwt);

        @PostMapping
        @Operation(summary = "장바구니 아이템 추가", description = "장바구니에 아이템을 추가합니다.")
        ResponseEntity<ReadCart.Response> addCart(@AuthenticationPrincipal JwtAuthentication jwt,
                        @RequestBody AddCartItemRequest request);

        @PutMapping
        @Operation(summary = "장바구니 아이템 수량 변경", description = "장바구니 아이템의 수량을 변경합니다.")
        ResponseEntity<ReadCart.Response> updateCartItemQuantity(@AuthenticationPrincipal JwtAuthentication jwt,
                        @RequestBody UpdateCartItemQuantityRequest request);

        @PatchMapping
        @Operation(summary = "장바구니 아이템 선택", description = "장바구니 아이템을 선택합니다.")
        ResponseEntity<ReadCart.Response> selectCartItem(@AuthenticationPrincipal JwtAuthentication jwt,
                        @RequestBody SelectCartItemRequest request);

        @DeleteMapping
        @Operation(summary = "장바구니 비우기", description = "장바구니를 비웁니다.")
        ResponseEntity<DeleteCartResponse> clearCart(@AuthenticationPrincipal JwtAuthentication jwt);

        @DeleteMapping("/items")
        @Operation(summary = "장바구니 아이템 삭제", description = "장바구니에서 아이템을 삭제합니다.")
        ResponseEntity<ReadCart.Response> deleteCartItems(@AuthenticationPrincipal JwtAuthentication jwt,
                        @RequestBody DeleteCartItemsRequest request);
}
