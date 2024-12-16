package com.xthreads.post_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentCreationRequest {
    private String userId;       // ID người dùng viết bình luận
    private String postId;       // ID bài đăng cần bình luận
    private String comment;      // Nội dung bình luận
}
