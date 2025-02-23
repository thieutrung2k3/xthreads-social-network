package com.xthreads.post_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xthreads.post_service.constant.ReactionType;
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
public class ReactionResponse {
    String postId;
    String accountId;
    LocalDate timestamp;
    ReactionType reactionType;
}
