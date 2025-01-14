package com.xthreads.auth_service.repository.client;

import com.xthreads.auth_service.configuration.CustomRequestInterceptor;
import com.xthreads.auth_service.dto.request.UserAvatarUpdateRequest;
import com.xthreads.auth_service.dto.request.UserCreationRequest;
import com.xthreads.auth_service.dto.request.UserUpdateRequest;
import com.xthreads.auth_service.dto.response.ApiResponse;
import com.xthreads.auth_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "user-service",
        url = "${app.services.user}",
        configuration = {CustomRequestInterceptor.class})
public interface UserClient {
    @PostMapping(value = "/information/register", produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse registerAccount(@RequestBody UserCreationRequest request);

    @GetMapping(value = "/information/get-all-users")
    UserResponse getAllUsers();

    @GetMapping(value = "/information/get/{accountID}")
    Optional<UserResponse> getUserByAccountID(@PathVariable String accountID);

    @PutMapping(value = "/information/update-user", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<UserResponse> updateInformationUser(@RequestBody UserUpdateRequest request);

    @PutMapping(value = "/information/update/avatar/{accountID}", produces = MediaType.APPLICATION_JSON_VALUE)
    void updateUserAvatar(@PathVariable String accountID, @RequestBody UserAvatarUpdateRequest request);
}
