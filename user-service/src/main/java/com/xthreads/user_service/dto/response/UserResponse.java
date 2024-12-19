package com.xthreads.user_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userID;
    String accountID;
    String firstName;
    String lastName;
    String gender;
    String address;
    String email;
    String urlProfilePicture;
    LocalDate dob;
}
