package com.xthreads.post_service.repository.client;

import com.xthreads.post_service.configuration.CustomRequestInterceptor;
import com.xthreads.post_service.dto.response.ApiResponse;
import com.xthreads.post_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;

@FeignClient(name = "auth-service",
            url = "${app.services.auth}",
            configuration = {CustomRequestInterceptor.class})
public interface AuthClient {
    @GetMapping(value = "/t/get-accId-from-token", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<ApiResponse<String>> getAccountIdFromToken(@RequestHeader("Authorization") String token);

    @GetMapping(value = "/get/{accountID}", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<ApiResponse<UserResponse>> getUserByAccountID(@PathVariable String accountID);
}
