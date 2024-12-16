package com.xthreads.post_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PostLikes")
public class PostLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "postID", nullable = false)
    private Posts post;
    private String userID;
    private LocalDateTime createdAt = LocalDateTime.now();
}
