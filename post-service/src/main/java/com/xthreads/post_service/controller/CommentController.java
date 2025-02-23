package com.xthreads.post_service.controller;

import com.xthreads.post_service.dto.request.CommentCreationRequest;
import com.xthreads.post_service.dto.request.CommentUpdateRequest;
import com.xthreads.post_service.dto.response.ApiResponse;
import com.xthreads.post_service.dto.response.CommentResponse;
import com.xthreads.post_service.exception.AppException;
import com.xthreads.post_service.exception.ErrorCode;
import com.xthreads.post_service.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    CommentService commentService;

    @GetMapping("/getAllPostComment/{postId}")
    public ApiResponse<List<CommentResponse>> getAllCommentByPostId(@PathVariable("postId") String postId){
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getAllCommentByPostId(postId))
                .build();
    }

    @PostMapping("/addComment")
    public ApiResponse<CommentResponse> addComment(@RequestBody CommentCreationRequest request){
        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.addComment(request, accountId))
                .build();
    }

    @PutMapping("/updateComment/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@RequestBody CommentUpdateRequest request,
                                                      @PathVariable("commentId") String commentId){
        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(request, accountId, commentId))
                .build();
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public ApiResponse<String> deleteComment(@PathVariable("commentId") String commentId){
        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();

        return ApiResponse.<String>builder()
                .result(commentService.deleteComment(accountId, commentId))
                .build();
    }
}
