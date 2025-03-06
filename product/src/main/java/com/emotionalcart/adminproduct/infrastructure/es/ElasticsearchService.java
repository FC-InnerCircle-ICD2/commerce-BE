package com.emotionalcart.adminproduct.infrastructure.es;

import com.emotionalcart.adminproduct.domain.event.ProductCreatedEvent;
import com.emotionalcart.adminproduct.infrastructure.es.http.ElasticsearchFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchFeignClient elasticsearchFeignClient;

    /**
     * es에 상품 적재
     */
    public void saveProduct(ProductCreatedEvent event) {
        elasticsearchFeignClient.saveProduct(event);
    }

}
