package com.emotionalcart.product.domain;

import org.springframework.stereotype.Component;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.provider.Provider;
import com.emotionalcart.product.infrastructure.repository.ProviderRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProviderDataProvider {
    private final ProviderRepository providerRepository;

    public Provider findProviderById(Long providerId) {
        return providerRepository.findByIdAndIsDeletedIsFalse(providerId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PROVIDER));
    }

}
