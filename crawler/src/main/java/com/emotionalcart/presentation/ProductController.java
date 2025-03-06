package com.emotionalcart.presentation;

import com.emotionalcart.application.RedisProductService;
import com.emotionalcart.domain.RedisProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crawler/products")
public class ProductController {

    private final RedisProductService redisProductService;

    /**
     * 상품 검색 API
     */
    @GetMapping("/search")
    public ResponseEntity<List<RedisProduct>> search(@RequestParam(value = "keyword") String keyword) {
        return ResponseEntity.ok(redisProductService.searchProducts(keyword));
    }

    /**
     * 유사 상품 추천 API
     */
    @GetMapping("/recommend/{productId}")
    public ResponseEntity<List<RedisProduct>> recommend(@PathVariable String productId) {
        return ResponseEntity.ok(redisProductService.recommendProducts(productId));
    }

}
