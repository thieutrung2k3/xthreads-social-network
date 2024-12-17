package com.xthreads.friendship_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String userId1;
    String userId2;
    LocalDateTime createAt;
}
