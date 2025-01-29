package com.emotionalcart.product.application.fixture;

import com.emotionalcart.product.presentation.dto.ReadProductsValidate;

import java.lang.reflect.Field;
import java.util.List;

public class ProductFixture {
    public static ReadProductsValidate.Request createReadProductValidateRequest(Long productId, Integer quantity, List<ReadProductsValidate.Request.OptionRequest> options) throws Exception {
        // Request 객체 생성
        ReadProductsValidate.Request request = new ReadProductsValidate.Request();

        // productId 필드 설정
        Field productIdField = ReadProductsValidate.Request.class.getDeclaredField("productId");
        productIdField.setAccessible(true);
        productIdField.set(request, productId);

        // quantity 필드 설정
        Field quantityField = ReadProductsValidate.Request.class.getDeclaredField("quantity");
        quantityField.setAccessible(true);
        quantityField.set(request, quantity);

        // productOptions 필드 설정
        Field optionsField = ReadProductsValidate.Request.class.getDeclaredField("productOptions");
        optionsField.setAccessible(true);
        optionsField.set(request, options);

        return request;
    }

    public static ReadProductsValidate.Request.OptionRequest createOptionRequest(Long optionId, Long detailId) throws Exception {
        // OptionRequest 객체 생성
        ReadProductsValidate.Request.OptionRequest optionRequest = new ReadProductsValidate.Request.OptionRequest();

        // productOptionId 필드 설정
        Field optionIdField = ReadProductsValidate.Request.OptionRequest.class.getDeclaredField("productOptionId");
        optionIdField.setAccessible(true);
        optionIdField.set(optionRequest, optionId);

        // productOptionDetailId 필드 설정
        Field detailIdField = ReadProductsValidate.Request.OptionRequest.class.getDeclaredField("productOptionDetailId");
        detailIdField.setAccessible(true);
        detailIdField.set(optionRequest, detailId);

        return optionRequest;
    }
}
