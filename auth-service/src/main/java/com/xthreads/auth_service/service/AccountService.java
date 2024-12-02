package com.xthreads.auth_service.service;

import com.xthreads.auth_service.constant.RoleConstant;
import com.xthreads.auth_service.dto.request.AccountCreationRequest;
import com.xthreads.auth_service.dto.request.UserCreationRequest;
import com.xthreads.auth_service.dto.response.AccountResponse;
import com.xthreads.auth_service.dto.response.ApiResponse;
import com.xthreads.auth_service.entity.Account;
import com.xthreads.auth_service.entity.Role;
import com.xthreads.auth_service.exception.AppException;
import com.xthreads.auth_service.exception.ErrorCode;
import com.xthreads.auth_service.mapper.AccountMapper;
import com.xthreads.auth_service.mapper.UserMapper;
import com.xthreads.auth_service.repository.AccountRepository;
import com.xthreads.auth_service.repository.RoleRepository;
import com.xthreads.auth_service.repository.client.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserClient userClient;
    UserMapper userMapper;

    public ApiResponse<AccountResponse> registerAccount(AccountCreationRequest request){
        Account account = accountMapper.toAccount(request);
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(RoleConstant.USER).ifPresent(roles::add);

        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRoles(roles);

        try{
            accountRepository.save(account);
        }catch (Exception e){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        UserCreationRequest userCreationRequest = userMapper.toUserCreationRequest(request);
        userCreationRequest.setAccountID(account.getId());
        userClient.registerAccount(userCreationRequest);

        return ApiResponse.<AccountResponse>builder()
                .result(accountMapper.toAccountResponse(account))
                .build();
    }
}
