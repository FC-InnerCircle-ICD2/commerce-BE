package com.emotionalcart.adminproduct.infrastructure.search.http;

import com.emotionalcart.adminproduct.domain.event.ProductCreatedEvent;
import com.emotionalcart.core.config.FeignClientConfig;
import com.emotionalcart.adminproduct.infrastructure.search.dto.IndexCreateBanner;
import com.emotionalcart.adminproduct.infrastructure.search.dto.IndexCreateProvider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "search-service", url = "${search.find.feign-endpoint}", path = "/api/v1", configuration = FeignClientConfig.class)
public interface SearchFeignClient {

    /**
     *
     * @param request
     */
    @PostMapping("/providers")
    void indexCreateProvider(@RequestBody IndexCreateProvider request);

    /**
     *
     * @param providerId
     */
    @DeleteMapping("/providers/{providerId}")
    void indexDeleteProvider(@PathVariable Long providerId);

    @PostMapping("/banners")
    void indexCreateBanner(@RequestBody IndexCreateBanner request);

    @DeleteMapping("/banners/{bannerId}")
    void indexDeleteBanner(@PathVariable Long bannerId);

    @PostMapping("/elastic/product")
    void indexCreateProduct(@RequestBody ProductCreatedEvent event);

}
