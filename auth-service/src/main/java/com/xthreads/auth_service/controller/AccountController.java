package com.xthreads.auth_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xthreads.auth_service.dto.request.AccountCreationRequest;
import com.xthreads.auth_service.dto.request.UserCreationRequest;
import com.xthreads.auth_service.dto.response.AccountResponse;
import com.xthreads.auth_service.dto.response.ApiResponse;
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
}
