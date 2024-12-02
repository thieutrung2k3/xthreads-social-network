package com.xthreads.api_gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xthreads.api_gateway.dto.response.ApiResponse;
import com.xthreads.api_gateway.exception.ErrorCode;
import com.xthreads.api_gateway.reponsitory.AuthClient;
import com.xthreads.api_gateway.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    AuthClient authClient;
    AuthService authService;

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    private List<String> PUBLIC_ENDPOINTS = List.of(
            "/auth/account/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info(path);
        log.info("apiPrefix: " + apiPrefix);
        if(PUBLIC_ENDPOINTS.stream().anyMatch(endPoint -> (apiPrefix + endPoint).equals(path))){
            return chain.filter(exchange);
        }
        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(authHeaders))
            return unauthenticated(exchange.getResponse());
        String token = authHeaders.getFirst().replace("Bearer: ", "");

        return authService.validateToken(token).flatMap(validateTokenResponseApiResponse -> {
            if(validateTokenResponseApiResponse.getResult().getValid()){
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
        ApiResponse apiResponse = ApiResponse.builder()
                .code(ErrorCode.UNAUTHENTICATED.getCode())
                .message(ErrorCode.UNAUTHENTICATED.getMessage())
                .build();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return response.writeWith(Mono.just(response.bufferFactory().wrap(objectMapper.writeValueAsBytes(apiResponse))));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
