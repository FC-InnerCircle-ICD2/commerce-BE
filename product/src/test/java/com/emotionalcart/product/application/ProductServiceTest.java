package com.emotionalcart.product.application;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.stock.Stock;
import com.emotionalcart.product.application.fixture.ProductFixture;
import com.emotionalcart.product.domain.CategoryDataProvider;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.product.domain.ProviderDataProvider;
import com.emotionalcart.product.domain.StockDataProvider;
import com.emotionalcart.product.domain.dto.ProductDetail;
import com.emotionalcart.product.infrastructure.StockSearchCondition;
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

    @Mock
    private StockDataProvider stockDataProvider;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDataProvider, categoryDataProvider, providerDataProvider, stockDataProvider);
    }

    @Test
    public void 상품이_존재하지_않을_때_NOT_FOUND_PRODUCT_예외_발생() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 201L));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, 1, optionRequests));

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductsValidate(requests)
        );

        assertEquals(ErrorCode.NOT_FOUND_PRODUCT.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품옵션상세값이_유효하지_않으면_NOT_FOUND_PRODUCT_OPTION_예외_발생() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 204L));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, 1, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 10000, 101L, 103L, 1000), // 필수 옵션
                new ProductDetail(1L, 20000, 102L, 104L, 0) // 선택 옵션
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
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(103L, 201L));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, 1, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 10000, 101L, 103L, 0)
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
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 201L));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, 100, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 100000, 101L, 201L, 10)
        );
        Mockito.when(productDataProvider.findAllProductDetail(Set.of(1L))).thenReturn(productDetails);
        Mockito.when(stockDataProvider.findStock(Mockito.any(StockSearchCondition.class))).thenReturn(Stock.of(1L, 10));

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.readProductsValidate(requests)
        );

        assertEquals(ErrorCode.OUT_OF_STOCK.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void 상품_검증_성공() throws Exception {
        List<ReadProductsValidate.Request.OptionRequest> optionRequests = List.of(ProductFixture.createOptionRequest(101L, 103L));
        List<ReadProductsValidate.Request> requests = List.of(ProductFixture.createReadProductValidateRequest(1L, 1, optionRequests));
        List<ProductDetail> productDetails = List.of(
                new ProductDetail(1L, 10000, 101L, 103L, null)
        );
        Mockito.when(productDataProvider.findAllProductDetail(Set.of(1L))).thenReturn(productDetails);
        Mockito.when(stockDataProvider.findStock(Mockito.any(StockSearchCondition.class))).thenReturn(Stock.of(1L, 10));

        assertDoesNotThrow(() -> productService.readProductsValidate(requests));
    }
}
