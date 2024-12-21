package com.xthreads.api_gateway.reponsitory;

import com.xthreads.api_gateway.dto.request.ValidateTokenRequest;
import com.xthreads.api_gateway.dto.response.ApiResponse;
import com.xthreads.api_gateway.dto.response.ValidateTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {
    @PostExchange(url = "/t/validate", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<ValidateTokenResponse>> validateToken(@RequestBody ValidateTokenRequest request);
}
