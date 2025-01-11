package com.emotionalcart.product.application;

import com.emotionalcart.product.domain.entity.ProductCategory;
import com.emotionalcart.product.domain.repository.ProductCategoryRespository;
import com.emotionalcart.product.presentation.dto.ReadProductCategoryResponse;
import com.emotionalcart.product.presentation.response.Pagination;
import com.emotionalcart.product.presentation.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductCategoryRespository productCategoryRespository;

    public Response<List<ReadProductCategoryResponse>> getAllProductCategories(Pageable pageable){

        Page<ProductCategory> productCategoryPage = productCategoryRespository.findAll(pageable);

        List<ReadProductCategoryResponse> productCategories = productCategoryPage.getContent()
                .stream()
                .map(ReadProductCategoryResponse::toDto)
                .collect(Collectors.toList());

        Pagination pagination = new Pagination().complete(productCategoryPage, pageable);

        return new Response<List<ReadProductCategoryResponse>>()
                .responseOk(productCategories, pagination);
    }
}
