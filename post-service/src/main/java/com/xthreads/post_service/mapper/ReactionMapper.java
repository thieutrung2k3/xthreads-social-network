package com.xthreads.post_service.mapper;

import com.xthreads.post_service.dto.request.ReactionCreationRequest;
import com.xthreads.post_service.dto.response.ReactionResponse;
import com.xthreads.post_service.entity.Reaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
    Reaction toReaction(ReactionCreationRequest request);
    ReactionResponse toReactionResponse(Reaction like);
}
