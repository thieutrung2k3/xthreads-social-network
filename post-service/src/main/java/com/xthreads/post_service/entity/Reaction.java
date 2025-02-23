package com.xthreads.post_service.entity;

import com.xthreads.post_service.constant.ReactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String reactionId;
    String postId;
    String accountId;
    LocalDate timestamp;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ReactionType reactionType;
}
