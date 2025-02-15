package com.emotionalcart.adminproduct.domain;

import com.emotionalcart.adminproduct.infrastructure.repository.AdminProviderRepository;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProviderDataProvider {
    private final AdminProviderRepository providerRepository;

    public void findProviderById(Long providerId) {
        providerRepository.findByIdAndIsDeletedIsFalse(providerId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PROVIDER));
    }
}
