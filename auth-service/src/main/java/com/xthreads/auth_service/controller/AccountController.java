package com.xthreads.auth_service.controller;

import com.xthreads.auth_service.dto.request.AccountCreationRequest;
import com.xthreads.auth_service.dto.response.AccountResponse;
import com.xthreads.auth_service.dto.response.ApiResponse;
import com.xthreads.auth_service.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/account")
public class AccountController {
    AccountService accountService;

    @PostMapping("/register")
    public ApiResponse<AccountResponse> registerAccount(@RequestBody AccountCreationRequest request){
        return accountService.registerAccount(request);
    }
}
