package com.xthreads.auth_service.mapper;

import com.xthreads.auth_service.dto.request.AccountCreationRequest;
import com.xthreads.auth_service.dto.request.UserCreationRequest;
import com.xthreads.auth_service.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserCreationRequest toUserCreationRequest(AccountCreationRequest request);
}
