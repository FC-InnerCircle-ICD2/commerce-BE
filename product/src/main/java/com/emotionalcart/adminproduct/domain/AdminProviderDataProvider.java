package com.emotionalcart.adminproduct.domain;

import com.emotionalcart.adminproduct.infrastructure.AdminProviders;
import com.emotionalcart.adminproduct.infrastructure.repository.AdminProviderRepository;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.provider.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminProviderDataProvider {
    private final AdminProviderRepository providerRepository;

    public Provider findProviderById(Long providerId) {
        return providerRepository.findByIdAndIsDeletedIsFalse(providerId)
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PROVIDER));
    }

    public Page<AdminProviders> findAllProviders(PageRequest pageRequest){
        return providerRepository.findAllProviders(pageRequest);
    }

    public Provider saveProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    public void updateProviderMemberId(Long providerId, Long memberId) {
        Provider provider = providerRepository.findByIdAndIsDeletedIsFalse(providerId)
            .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PROVIDER));
        provider.updateMemberId(memberId);
        saveProvider(provider);
    }

}
