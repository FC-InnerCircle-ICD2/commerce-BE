package com.emotionalcart.product.presentation.dto;

import java.time.LocalDateTime;

import com.emotionalcart.core.feature.product.Provider;

import lombok.Data;

public class ReadProviders {

    @Data
    public static class Response {
        private Long providerId;
        private String name;
        private String description;

        public Response(Provider provider) {
            this.providerId = provider.getId();
            this.name = provider.getName();
            this.description = provider.getDescription();
        }

        public static Response toResponse(Provider provider) {
            return new Response(provider);
        }
    }

}
