package com.xthreads.auth_service.mapper;

import com.xthreads.auth_service.dto.request.AccountCreationRequest;
import com.xthreads.auth_service.dto.request.AccountUpdateRequest;
import com.xthreads.auth_service.dto.response.AccountResponse;
import com.xthreads.auth_service.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountCreationRequest request);
    AccountResponse toAccountResponse(Account account);

    @Mapping(target = "roles", ignore = true)
    void updateAccount(@MappingTarget Account account, AccountUpdateRequest request);
}
