package com.emotionalcart.infra.repository;

import com.emotionalcart.domain.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByName(String providerName);

}
