package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.adminproduct.infrastructure.AdminProducts;
import com.emotionalcart.adminproduct.infrastructure.AdminProviders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AdminQueryDslProviderRepository {
    Page<AdminProviders> findAllProviders(PageRequest pageRequest);
}
