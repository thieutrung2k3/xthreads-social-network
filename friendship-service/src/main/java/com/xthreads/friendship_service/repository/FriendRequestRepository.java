package com.xthreads.friendship_service.repository;

import com.xthreads.friendship_service.model.FriendRequest;
import com.xthreads.friendship_service.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    Optional<FriendRequest> findBySenderIdAndReceiverId(String senderId, String receiverId);
    List<FriendRequest> findAllByReceiverIdAndStatus(String userId, Status status);
}
