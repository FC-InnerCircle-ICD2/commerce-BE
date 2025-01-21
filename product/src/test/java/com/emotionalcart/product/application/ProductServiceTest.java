package com.emotionalcart.product.application;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.product.application.fixture.ProductFixture;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.presentation.dto.ReadProductValidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDataProvider productDataProvider;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDataProvider);
    }

    @Test
    public void 상품이_존재하지_않을_때_NOT_FOUND_PRODUCT_예외_발생() throws Exception {
        List<ReadProductValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 201L, 2));
        List<ReadProductValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductValidate(requests)
        );

        assertEquals(ErrorCode.NOT_FOUND_PRODUCT.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품_필수옵션을_선택하지_않으면_REQUIRED_OPTION_MISSING_예외_발생() throws Exception {
        List<ReadProductValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(102L, 201L, 2));
        List<ReadProductValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 101L, true, 201L, 10), // 필수 옵션
                new ProductDetail(1L, 102L, false, 202L, 10) // 선택 옵션
        );
        Mockito.when(productDataProvider.findAllProductData(Set.of(1L)))
                .thenReturn(productDetails);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductValidate(requests)
        );

        assertEquals(ErrorCode.REQUIRED_OPTION_MISSING.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품옵션상세값이_유효하지_않으면_NOT_FOUND_PRODUCT_OPTION_예외_발생() throws Exception {
        List<ReadProductValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 204L, 2));
        List<ReadProductValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 101L, true, 201L, 10), // 유효한 옵션
                new ProductDetail(1L, 102L, false, 202L, 10) // 유효한 옵션
        );
        Mockito.when(productDataProvider.findAllProductData(Set.of(1L)))
                .thenReturn(productDetails);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductValidate(requests)
        );

        assertEquals(ErrorCode.NOT_FOUND_PRODUCT_OPTION.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품옵션값이_유효하지_않으면_NOT_FOUND_PRODUCT_OPTION_예외_발생() throws Exception {
        List<ReadProductValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(103L, 201L, 2));
        List<ReadProductValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 101L, true, 201L, 10)
        );
        Mockito.when(productDataProvider.findAllProductData(Set.of(1L)))
                .thenReturn(productDetails);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductValidate(requests)
        );

        assertEquals(ErrorCode.NOT_FOUND_PRODUCT_OPTION.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품_재고가_부족하면_OUT_OF_STOCK_예외_발생() throws Exception {
        List<ReadProductValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 201L, 20));
        List<ReadProductValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 101L, true, 201L, 10)
        );
        Mockito.when(productDataProvider.findAllProductData(Set.of(1L)))
                .thenReturn(productDetails);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductValidate(requests)
        );

        assertEquals(ErrorCode.OUT_OF_STOCK.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품_검증_성공() throws Exception {
        List<ReadProductValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 201L, 2));
        List<ReadProductValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 101L, true, 201L, 10)
        );
        Mockito.when(productDataProvider.findAllProductData(Set.of(1L)))
                .thenReturn(productDetails);

        assertDoesNotThrow(() -> productService.readProductValidate(requests));
    }
}