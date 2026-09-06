package com.Ashish.airBnbClone.advice;


import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // true means: Yes, apply beforeBodyWrite()
    }

    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        List<String> excludedRoutes = List.of("/v3/api-docs", "/actuator");
        // /v3/api-docs — Swagger/OpenAPI endpoint that provides the API documentation in JSON format.
        // /actuator — Spring Boot Actuator endpoint used to expose application monitoring/management information.

        boolean isExcluded = excludedRoutes
                .stream()
                .anyMatch(route -> request.getURI().getPath().contains(route));

        if (body == null) {
            return null;
        }

        if (body instanceof ApiResponse<?> || isExcluded) {
            return body;
        }

        return new ApiResponse<>(body);
    }
}
