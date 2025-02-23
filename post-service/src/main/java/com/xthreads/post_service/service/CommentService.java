package com.xthreads.post_service.service;

import ch.qos.logback.core.util.StringUtil;
import com.xthreads.post_service.dto.request.CommentCreationRequest;
import com.xthreads.post_service.dto.request.CommentUpdateRequest;
import com.xthreads.post_service.dto.response.ApiResponse;
import com.xthreads.post_service.dto.response.CommentResponse;
import com.xthreads.post_service.entity.Comment;
import com.xthreads.post_service.exception.AppException;
import com.xthreads.post_service.exception.ErrorCode;
import com.xthreads.post_service.mapper.CommentMapper;
import com.xthreads.post_service.repository.CommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    public List<CommentResponse> getAllCommentByPostId(String postId){
        return commentRepository.getAllByPostId(postId).stream().map(commentMapper::toCommentResponse).toList();
    }

    public CommentResponse addComment(CommentCreationRequest request, String accountId){
        log.info(request.getAccountId() + " " + accountId);
        if(!request.getAccountId().equals(accountId))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        Comment comment = commentMapper.toComment(request);
        comment.setTimestamp(LocalDate.now());
        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public CommentResponse updateComment(CommentUpdateRequest request, String accountId, String commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        if(!Objects.equals(accountId, comment.getAccountId()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        commentMapper.updateComment(comment, request);
        comment.setTimestamp(LocalDate.now());
        comment = commentRepository.save(comment);

        return commentMapper.toCommentResponse(comment);
    }

    public String deleteComment(String accountId, String commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        if(!Objects.equals(accountId, comment.getAccountId()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        commentRepository.deleteById(commentId);

        return "Comment has been deleted.";
    }
}
