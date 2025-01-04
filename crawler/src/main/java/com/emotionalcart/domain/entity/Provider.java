package com.emotionalcart.domain.entity;

import com.emotionalcart.domain.generator.IdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Provider extends BaseEntity {

    @Id
    @IdGenerator
    private Long id;

    private String name;

    private String description;

    public static Provider of(String providerName) {
        Provider provider = new Provider();
        provider.name = providerName;
        return provider;
    }

}
