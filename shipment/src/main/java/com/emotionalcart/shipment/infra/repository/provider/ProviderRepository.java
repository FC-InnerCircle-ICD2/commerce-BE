package com.emotionalcart.shipment.infra.repository.provider;

import com.emotionalcart.shipment.infra.repository.provider.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByMemberId(Long id);

}
