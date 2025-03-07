package com.emotionalcart.product.domain.support;

import com.emotionalcart.product.infrastructure.elasticsearch.dto.ElasticProduct;

import java.util.List;
import java.util.stream.Collectors;

public class ElasticProducts {

    private final List<ElasticProduct> elasticProducts;

    public ElasticProducts(List<ElasticProduct> products) {
        this.elasticProducts = products;
    }

    public static ElasticProducts from(List<ElasticProduct> products){
        return new ElasticProducts(products);
    }

    public List<Long> ids() {
        return elasticProducts.stream().map(ElasticProduct::getId).toList();
    }

    public List<Long> getCategoryIds() {
        return this.elasticProducts.stream()
            .map(ElasticProduct::getCategoryId)
            .collect(Collectors.toList());
    }

    public List<Long> getProviderIds() {
        return this.elasticProducts.stream()
            .map(ElasticProduct::getProviderId)
            .collect(Collectors.toList());
    }

}
