package com.xthreads.auth_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xthreads.auth_service.dto.request.*;
import com.xthreads.auth_service.dto.response.AccountResponse;
import com.xthreads.auth_service.dto.response.ApiResponse;
import com.xthreads.auth_service.dto.response.UserResponse;
import com.xthreads.auth_service.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/account")
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    AccountService accountService;
    ObjectMapper objectMapper;
    @PostMapping("/register")
    public ApiResponse<AccountResponse> registerAccount(@RequestPart("file") MultipartFile file,
                                                        @RequestPart("user") String data) throws JsonProcessingException {
        AccountCreationRequest request = objectMapper.readValue(data, AccountCreationRequest.class);
        log.info(data);
        return accountService.registerAccount(file, request);
    }

    @PutMapping("/update/password/{accountID}")
    public ApiResponse<AccountResponse> updatePassword(@PathVariable String accountID, @RequestBody AccountUpdateRequest request){
        return accountService.updateAccount(accountID, request);
    }

    @PutMapping("/update/all/{accountID}")
    public ApiResponse<UserResponse> updateUserInformation(@RequestPart("file") MultipartFile file,
                                                           @RequestPart("user") String data) throws JsonProcessingException {
        UserUpdateRequest request = objectMapper.readValue(data, UserUpdateRequest.class);
        return ApiResponse.<UserResponse>builder()
                .result(accountService.updateUserInformation(file, request))
                .build();
    }

    @PutMapping("/update/avatar/{accountID}")
    public ApiResponse<Void> updateUserAvatar(@PathVariable String accountID,
                                              @RequestPart("file") MultipartFile file){
        accountService.updateUserAvatar(accountID, file);
        return ApiResponse.<Void>builder()
                .build();
    }
}
