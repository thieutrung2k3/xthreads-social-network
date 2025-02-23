package com.xthreads.post_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.xthreads.post_service.configuration.ReactionTypeDeserializer;
import com.xthreads.post_service.constant.ReactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReactionCreationRequest {
    String postId;
    ReactionType reactionType;
}
