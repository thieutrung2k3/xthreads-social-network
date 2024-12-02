package com.xthreads.api_gateway.service;

import com.xthreads.api_gateway.dto.request.ValidateTokenRequest;
import com.xthreads.api_gateway.dto.response.ApiResponse;
import com.xthreads.api_gateway.dto.response.ValidateTokenResponse;
import com.xthreads.api_gateway.reponsitory.AuthClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    AuthClient authClient;

    public Mono<ApiResponse<ValidateTokenResponse>> validateToken(String token){
        return authClient.validateToken(ValidateTokenRequest.builder()
                        .token(token)
                        .build());
    }
}
