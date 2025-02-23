package com.xthreads.post_service.mapper;

import com.xthreads.post_service.dto.request.CommentCreationRequest;
import com.xthreads.post_service.dto.request.CommentUpdateRequest;
import com.xthreads.post_service.dto.response.CommentResponse;
import com.xthreads.post_service.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentCreationRequest request);
    CommentResponse toCommentResponse(Comment comment);
    void updateComment(@MappingTarget Comment comment, CommentUpdateRequest request);
}
