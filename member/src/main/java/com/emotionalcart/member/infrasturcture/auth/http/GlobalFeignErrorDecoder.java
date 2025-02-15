package com.emotionalcart.member.infrasturcture.auth.http;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ErrorResponse;
import com.emotionalcart.core.exception.MemberException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class GlobalFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());

        if (!httpStatus.is2xxSuccessful()) {
            try {
                // Response body를 문자열로 변환
                String responseBody = responseBodyToString(response);

                // ErrorResponse 파싱 시도
                ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

                switch (errorResponse.getErrorCode()) {
                    case "AUTH-0002" -> throw new MemberException(ErrorCode.UNAUTHORIZED);
                    case "AUTH-0003" -> throw new MemberException(ErrorCode.TOKEN_EXPIRED);
                    case "AUTH-0004" -> throw new MemberException(ErrorCode.INVALID_TOKEN);
                    default -> throw new MemberException(ErrorCode.MEMBER_AUTH_ERROR);

                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return errorDecoder.decode(methodKey, response);
    }

    /**
     * Response body를 문자열로 변환하는 메서드
     */
    private String responseBodyToString(Response response) throws IOException {
        if (response.body() == null) {
            return "";
        }

        try (var reader = response.body().asInputStream()) {
            return new String(reader.readAllBytes());
        }
    }
}
