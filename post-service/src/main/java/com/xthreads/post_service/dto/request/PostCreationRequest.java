package com.xthreads.post_service.dto.request;

import com.xthreads.post_service.constant.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {
    String accountID;
    String content;
    Status status;
}
