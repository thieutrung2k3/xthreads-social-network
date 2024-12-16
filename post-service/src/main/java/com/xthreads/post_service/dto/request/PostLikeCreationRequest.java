package com.xthreads.post_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLikeCreationRequest {
    private String userId;       // ID người dùng thích bài đăng
    private String postId;       // ID bài đăng được thích
}
