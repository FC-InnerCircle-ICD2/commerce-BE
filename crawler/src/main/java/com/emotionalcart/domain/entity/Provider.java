package com.emotionalcart.domain.entity;

import com.emotionalcart.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Provider extends BaseEntity {

    @Id
    @IdGenerator
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Product> products;

    public static Provider of(String providerName) {
        Provider provider = new Provider();
        provider.name = providerName;
        return provider;
    }

}
