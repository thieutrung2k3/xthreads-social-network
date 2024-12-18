package com.xthreads.friendship_service.repository;

import com.xthreads.friendship_service.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    Optional<FriendRequest> findBySenderIdAndReceiverId(String senderId, String receiverId);
}
