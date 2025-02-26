package com.emotionalcart.adminproduct.domain;

import com.emotionalcart.adminproduct.infrastructure.repository.AdminProviderRepository;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.provider.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProviderDataProvider {
    private final AdminProviderRepository providerRepository;

    public Provider findProviderById(Long providerId) {
        return providerRepository.findByIdAndIsDeletedIsFalse(providerId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PROVIDER));
    }
}
