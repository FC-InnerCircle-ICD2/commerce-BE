package com.emotionalcart.product.application;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.product.application.fixture.ProductFixture;
import com.emotionalcart.product.domain.CategoryDataProvider;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.ProviderDataProvider;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.presentation.dto.ReadProductsValidate;
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

    @Mock
    private CategoryDataProvider categoryDataProvider;

    @Mock
    private ProviderDataProvider providerDataProvider;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDataProvider, categoryDataProvider, providerDataProvider);
    }

    @Test
    public void 상품이_존재하지_않을_때_NOT_FOUND_PRODUCT_예외_발생() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 201L, 2));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductsValidate(requests)
        );

        assertEquals(ErrorCode.NOT_FOUND_PRODUCT.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품_필수옵션을_선택하지_않으면_REQUIRED_OPTION_MISSING_예외_발생() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(102L, 201L, 2));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 10000, 101L, true, 201L, 1000, 10), // 필수 옵션
                new ProductDetail(1L, 20000, 102L, false, 202L, null, 10) // 선택 옵션
        );
        Mockito.when(productDataProvider.findAllProductDetail(Set.of(1L)))
                .thenReturn(productDetails);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductsValidate(requests)
        );

        assertEquals(ErrorCode.REQUIRED_OPTION_MISSING.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품옵션상세값이_유효하지_않으면_NOT_FOUND_PRODUCT_OPTION_예외_발생() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 204L, 2));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 10000, 101L, true, 201L, 1000, 10), // 필수 옵션
                new ProductDetail(1L, 20000, 102L, false, 202L, null, 10) // 선택 옵션
        );
        Mockito.when(productDataProvider.findAllProductDetail(Set.of(1L)))
                .thenReturn(productDetails);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductsValidate(requests)
        );

        assertEquals(ErrorCode.NOT_FOUND_PRODUCT_OPTION.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품옵션값이_유효하지_않으면_NOT_FOUND_PRODUCT_OPTION_예외_발생() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(103L, 201L, 2));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 10000, 101L, true, 201L, null, 10)
        );
        Mockito.when(productDataProvider.findAllProductDetail(Set.of(1L)))
                .thenReturn(productDetails);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductsValidate(requests)
        );

        assertEquals(ErrorCode.NOT_FOUND_PRODUCT_OPTION.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품_재고가_부족하면_OUT_OF_STOCK_예외_발생() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 201L, 20));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 100000, 101L, true, 201L, null, 10)
        );
        Mockito.when(productDataProvider.findAllProductDetail(Set.of(1L)))
                .thenReturn(productDetails);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductsValidate(requests)
        );

        assertEquals(ErrorCode.OUT_OF_STOCK.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품_검증_성공() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 201L, 2));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 10000, 101L, true, 201L, null, 10)
        );
        Mockito.when(productDataProvider.findAllProductDetail(Set.of(1L)))
                .thenReturn(productDetails);

        assertDoesNotThrow(() -> productService.readProductsValidate(requests));
    }
}
