package com.xthreads.friendship_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendShip {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;


}
