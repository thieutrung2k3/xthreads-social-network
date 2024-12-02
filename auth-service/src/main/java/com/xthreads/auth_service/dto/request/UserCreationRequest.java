package com.xthreads.auth_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String accountID;
    String firstName;
    String lastName;
    String address;
    String email;
    String urlProfilePicture;
    LocalDate dob;
}
