package com.xthreads.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String userID;
    String firstName;
    String lastName;
    String gender;
    String address;
    String email;
    String urlProfilePicture;
    LocalDate dob;
}
