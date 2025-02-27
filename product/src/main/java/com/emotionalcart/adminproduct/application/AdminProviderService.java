package com.emotionalcart.adminproduct.application;

import com.emotionalcart.adminproduct.domain.AdminProviderDataProvider;
import com.emotionalcart.adminproduct.infrastructure.AdminProducts;
import com.emotionalcart.adminproduct.infrastructure.AdminProviders;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProductsResponse;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProvidersRequest;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProvidersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProviderService {

    private final AdminProviderDataProvider adminProviderDataProvider;

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
}
