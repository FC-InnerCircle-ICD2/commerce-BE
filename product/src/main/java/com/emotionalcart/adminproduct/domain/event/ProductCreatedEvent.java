package com.emotionalcart.adminproduct.domain.event;

import lombok.Getter;

import java.util.List;

public record ProductCreatedEvent(
    Long id,
    String name,
    String description,
    int price,
    Long providerId,
    String providerName,
    Long categoryId,
    String categoryName,
    List<Option> options
) {

    public record Option(Long id, String optionName, List<Detail> details) {

    }

    public record Detail(Long id, String detailName, int additionalPrice) {

    }

}