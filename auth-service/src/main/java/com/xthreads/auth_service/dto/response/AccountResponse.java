package com.xthreads.auth_service.dto.response;

import com.xthreads.auth_service.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    String username;
    Set<Role> roles;
}
