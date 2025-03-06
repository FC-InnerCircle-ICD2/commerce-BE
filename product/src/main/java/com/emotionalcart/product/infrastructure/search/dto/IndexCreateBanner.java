package com.emotionalcart.product.infrastructure.search.dto;

import java.time.LocalDateTime;

public class IndexCreateBanner {

    private Long id;

    private Boolean deleted;

    private String title;

    private String description;

    private Integer bannerOrder;

    private LocalDateTime createdAt;

    public static IndexCreateBanner of(
        Long id,
        Boolean deleted,
        String title,
        String description,
        Integer bannerOrder,
        LocalDateTime createdAt
    ) {
        IndexCreateBanner request = new IndexCreateBanner();
        request.id = id;
        request.deleted = deleted;
        request.title = title;
        request.description = description;
        request.bannerOrder = bannerOrder;
        request.createdAt = createdAt;
        return request;
    }
}
