package com.xthreads.post_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Posts")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userID;
    private String content;
    private String imageURL;
    private LocalDate createAt;
    private LocalDate updateAt;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PUBLIC;


    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private Set<PostLikes> postLikes = new HashSet<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private Set<PostComments> postComments = new HashSet<>();
    public enum Status {
        PUBLIC, PRIVATE, FRIEND
    }

    public void addLike(PostLikes like) {
        postLikes.add(like);
        like.setPost(this);
    }

    public void addComment(PostComments comment) {
        postComments.add(comment);
        comment.setPost(this);
    }




}
