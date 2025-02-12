package com.emotionalcart.adminproduct.application;

import com.emotionalcart.adminproduct.domain.AdminProductDataProvider;
import com.emotionalcart.s3.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProductService {
    private final AdminProductDataProvider adminProductDataProvider;
    private final S3Utils s3Utils;


}
