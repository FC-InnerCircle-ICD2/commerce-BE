package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "백오피스 판매처 (공급자) API", description = "백오피스 판매처 (공급자) 관련 API")
public interface AdminProviderControllerDocs {

    @Operation(summary = "판매처 (공급자) 목록 조회")
    public ResponseEntity<Page<ReadAdminProvidersResponse>> readProviders(ReadAdminProvidersRequest request);
}
