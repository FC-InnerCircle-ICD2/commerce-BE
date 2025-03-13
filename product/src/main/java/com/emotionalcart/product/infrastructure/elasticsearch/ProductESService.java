package com.emotionalcart.product.infrastructure.elasticsearch;

import com.emotionalcart.product.infrastructure.elasticsearch.dto.ESSearchRequest;
import com.emotionalcart.product.infrastructure.elasticsearch.dto.ElasticProduct;
import com.emotionalcart.product.domain.dto.ProductSearch;
import com.emotionalcart.product.infrastructure.elasticsearch.http.ElasticFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductESService {

    private final ElasticFeignClient elasticFeignClient;

    public List<ElasticProduct> searchProduct(ProductSearch productSearch) {
        ESSearchRequest esSearchRequest = ESSearchRequest.convert(productSearch);

        try{
            ResponseEntity<List<ElasticProduct>> response = elasticFeignClient.searchProducts(
                esSearchRequest.getKeyword(),
                esSearchRequest.getSortOption(),
                esSearchRequest.getPage(),
                esSearchRequest.getSize(),
                esSearchRequest.getMinPrice(),
                esSearchRequest.getMaxPrice()
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        }catch (Exception e){
            return Collections.emptyList();
        }
    }

    public List<ElasticProduct> getSimilarProducts(Long productId, Integer requestCount){
        ResponseEntity<List<ElasticProduct>> response = elasticFeignClient.getSimilarProducts(String.valueOf(productId), requestCount);
        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }
}
