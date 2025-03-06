package com.emotionalcart.adminproduct.infrastructure.search.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexCreateProvider {

    /**
     * 판매처 아이디
     */
    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    public static IndexCreateProvider of(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt
    ) {
        IndexCreateProvider request = new IndexCreateProvider();
        request.id = id;
        request.name = name;
        request.description = description;
        request.createdAt = createdAt;
        return request;
    }

}
