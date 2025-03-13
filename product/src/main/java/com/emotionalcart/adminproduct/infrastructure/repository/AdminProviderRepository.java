package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.core.feature.provider.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminProviderRepository extends JpaRepository<Provider, Long>, AdminQueryDslProviderRepository {
    Optional<Provider> findByIdAndIsDeletedIsFalse(Long providerId);
}
