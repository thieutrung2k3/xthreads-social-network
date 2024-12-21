package com.xthreads.api_gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xthreads.api_gateway.dto.response.ApiResponse;
import com.xthreads.api_gateway.service.AuthService;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    AuthService authService;
    ObjectMapper objectMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    @NonFinal
    private List<String> PUBLIC_ENDPOINTS = List.of(
            "/auth/account/register",
            "/user/information/get/**",
            "/user/image/**",
            "/user-post/image/**",
            "/auth/t/login",
            "/auth/t/logout",
            "/auth/t/validate",
            "/chat/**",
            "/user-post/post/get-all",
            "/user-post/post/get/**"
    );

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("Request path: {}", path);
        log.info("API Prefix: {}", apiPrefix);
        if(PUBLIC_ENDPOINTS.stream().anyMatch(endpoint ->
                pathMatcher.match(apiPrefix + endpoint, path))){
            log.info("Public endpoint accessed: {}", path);
            return chain.filter(exchange);
        }
        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(authHeaders)) {
            log.info("here");
            return unauthenticated(exchange.getResponse());
        }
        String token = authHeaders.getFirst().replace("Bearer ", "");
        log.info(token);
        return authService.validateToken(token).flatMap(validateTokenResponseApiResponse -> {
            log.info(String.valueOf(validateTokenResponseApiResponse.getResult().isValid()));
            if(validateTokenResponseApiResponse.getResult().isValid()){
                return chain.filter(exchange);
            }
            else{
                return unauthenticated(exchange.getResponse());
            }
        });

    }


    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response){
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}