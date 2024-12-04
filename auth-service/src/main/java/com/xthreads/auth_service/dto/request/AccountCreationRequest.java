package com.xthreads.auth_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreationRequest {
    String username;
    String password;
    String firstName;
    String lastName;
    String gender;
    String address;
    String email;
    String urlProfilePicture;
    LocalDate dob;
}
