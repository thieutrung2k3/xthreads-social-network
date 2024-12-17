package com.xthreads.friendship_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String senderId;
    String receiverId;

    @Enumerated(EnumType.STRING)
    Status status;

    LocalDateTime createAt;
    LocalDateTime updateAt;
}
