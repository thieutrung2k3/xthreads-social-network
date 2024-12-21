package com.xthreads.post_service.dto.response;

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
public class PostResponse {
    String postID;
    String accountID;
    String content;
    String imageUrl;
    LocalDate createdAt;
    LocalDate updatedAt;
    Status status;
}
