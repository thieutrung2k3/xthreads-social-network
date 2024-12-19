package com.xthreads.post_service.mapper;

import com.xthreads.post_service.dto.request.PostCreationRequest;
import com.xthreads.post_service.dto.request.PostUpdateRequest;
import com.xthreads.post_service.dto.response.PostResponse;
import com.xthreads.post_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostCreationRequest request);
    PostResponse toPostResponse(Post post);
    void updatePost(@MappingTarget Post post, PostUpdateRequest request);
}
