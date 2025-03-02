package com.emotionalcart.product.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DeleteCartItemsRequest {

    private List<String> itemIds;

}
