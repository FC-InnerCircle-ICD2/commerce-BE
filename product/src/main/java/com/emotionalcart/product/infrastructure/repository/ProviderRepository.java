package com.emotionalcart.product.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emotionalcart.core.feature.provider.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findByIdAndIsDeletedIsFalse(Long providerId);

    List<Provider> findAllByIdIn(List<Long> providerIds);
}
