package com.xthreads.auth_service.repository.client;

import com.xthreads.auth_service.configuration.CustomRequestInterceptor;
import com.xthreads.auth_service.dto.request.UserCreationRequest;
import com.xthreads.auth_service.dto.response.ApiResponse;
import com.xthreads.auth_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service",
        url = "${app.services.user}",
        configuration = {CustomRequestInterceptor.class})
public interface UserClient {
    @PostMapping(value = "/information/register", produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse registerAccount(@RequestBody UserCreationRequest request);
    @GetMapping(value = "/information/get-all-users")
    UserResponse getAllUsers();
}
