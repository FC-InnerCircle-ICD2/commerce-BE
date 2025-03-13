package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.provider.Provider;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

public class ReadProviders {

    @Data
    public static class Response {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String name;
        private String description;

        public Response(Provider provider) {
            this.id = provider.getId();
            this.name = provider.getName();
            this.description = provider.getDescription();
        }

        public static Response toResponse(Provider provider) {
            return new Response(provider);
        }
    }

}
