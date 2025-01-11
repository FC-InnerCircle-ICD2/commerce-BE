package com.emotionalcart.product.presentation.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Response<T> {

    @JsonProperty("content")
    private T value;

    @JsonProperty("pagination")
    private Pagination pagination;

    // pagination = null 인 경우, 사용하지 않아도 무방
    @JsonIgnore
    public Response<T> responseOk(T value) {
        this.value = value;
        this.pagination = null;
        return this;
    }

    @JsonIgnore
    public Response<T> responseOk(T value, Pagination pagination) {
        this.value = value;
        this.pagination = pagination;
        return this;
    }

}
