package com.emotionalcart.adminproduct.presentation.dto;

import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.provider.Provider;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateProviderRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;

    public Provider toEntity() {

        return Provider.of(
                name,
                description
        );
    }
}


