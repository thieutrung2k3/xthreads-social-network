package com.xthreads.post_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PostComments")
public class PostComments {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "postID")
    private Posts post;
    private String userID;

    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();
}
