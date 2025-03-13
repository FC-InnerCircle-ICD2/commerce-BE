package com.emotionalcart.adminproduct.application;

import com.emotionalcart.adminproduct.domain.AdminProviderDataProvider;
import com.emotionalcart.adminproduct.infrastructure.AdminProviders;
import com.emotionalcart.adminproduct.presentation.dto.*;
import com.emotionalcart.core.feature.provider.Provider;
import com.emotionalcart.adminproduct.infrastructure.search.SearchService;
import com.emotionalcart.adminproduct.infrastructure.search.dto.IndexCreateProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProviderService {

    private final AdminProviderDataProvider adminProviderDataProvider;
    private final SearchService searchService;

    public Page<ReadAdminProvidersResponse> readProviders(ReadAdminProvidersRequest request) {
        Page<AdminProviders> providers = adminProviderDataProvider.findAllProviders(request.getPageable());
        List<ReadAdminProvidersResponse> responseList = providers.getContent().stream()
            .map(this::mapToResponse)
            .toList();
        return new PageImpl<>(responseList, request.getPageable(), providers.getTotalElements());
    }

    private ReadAdminProvidersResponse mapToResponse(AdminProviders provider) {
        return new ReadAdminProvidersResponse(
            provider.getId(),
            provider.getName(),
            provider.getDescription()
        );
    }

    public CreateProviderResponse createProvider(CreateProviderRequest request) {

        Provider provider = request.toEntity();

        Provider savedProvider = adminProviderDataProvider.saveProvider(provider);

        searchService.indexCreateProvider(IndexCreateProvider.of(savedProvider.getId(), savedProvider.getName(), savedProvider.getDescription(), savedProvider.getCreatedAt()));

        return new CreateProviderResponse(savedProvider.getId());
    }

    public ReadProviderResponse readProvider(Long providerId) {
        Provider provider = adminProviderDataProvider.findProviderById(providerId);

        return new ReadProviderResponse(provider.getId(), provider.getName(), provider.getDescription(), provider.getMemberId());
    }

    public void updateProviderMemberId(UpdateProviderMemberIdRequest request) {
        adminProviderDataProvider.updateProviderMemberId(request.getProviderId(), request.getMemberId());
    }

    /**
     * 판매처 삭제 (논리삭제 isDeleted = ture)
     */
    public void deleteProvider(Long providerId) {
        Provider provider =adminProviderDataProvider.findProviderById(providerId);
        provider.delete();
        searchService.indexDeleteProvider(providerId);
    }
}
