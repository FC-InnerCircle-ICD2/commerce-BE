package com.emotionalcart.domain;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisProduct {

    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    @Setter
    private List<Float> vector;

}
