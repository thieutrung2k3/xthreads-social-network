package com.xthreads.friendship_service.repository;

import com.xthreads.friendship_service.model.Friends;
import org.springframework.boot.autoconfigure.container.ContainerImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, String> {
    Optional<Friends> findByUserId1AndUserId2(String userId1, String userId2);

//    List<Friends> findAllByUserId(String userId);

    Optional<Friends> findAllByUserId1AndUserId2(String userId1, String userId2);

    List<Friends> findAllByUserId1OrUserId2(String userId1, String userId2);
}
