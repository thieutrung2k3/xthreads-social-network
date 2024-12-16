package com.xthreads.post_service.dto.request;

import com.xthreads.post_service.entity.PostComments;
import com.xthreads.post_service.entity.PostLikes;
import com.xthreads.post_service.entity.Posts;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class postCreationRequest {
    private String userID;
    private String content;
    private String imageURL;
    private Posts.Status status;
}
