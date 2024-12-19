package com.xthreads.friendship_service.service;

import com.xthreads.friendship_service.dto.request.AcceptFriendRequest;
import com.xthreads.friendship_service.dto.request.RefuseFriendRequest;
import com.xthreads.friendship_service.dto.request.SendFriendRequest;
import com.xthreads.friendship_service.exception.ErrorCode;
import com.xthreads.friendship_service.model.FriendRequest;
import com.xthreads.friendship_service.model.Friends;
import com.xthreads.friendship_service.model.Status;
import com.xthreads.friendship_service.exception.AppException;
import com.xthreads.friendship_service.repository.FriendRequestRepository;
import com.xthreads.friendship_service.repository.FriendsRepository;
import com.xthreads.friendship_service.validation.FriendRequestValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendShipService {
    final FriendRequestRepository friendRequestRepository;
    final FriendsRepository friendsRepository;
    final FriendRequestValidator validator;

    // Add Friend Request
    public void sendFriendRequest(SendFriendRequest request) {
        validator.validateSendRequest(request);
        FriendRequest friendRequest = FriendRequest.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .status(Status.PENDING)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        friendRequestRepository.save(friendRequest);
    }

    // Accept Friend Request
    public void acceptFriendRequest(AcceptFriendRequest request){
        FriendRequest friendRequest = friendRequestRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        if (friendRequest.getStatus() != Status.PENDING) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        // Update status
        friendRequest.setStatus(Status.ACCEPTED);
        friendRequest.setUpdateAt(LocalDateTime.now());
        friendRequestRepository.save(friendRequest);

        // Save to Friends
        Friends friends = Friends.builder()
                .userId1(friendRequest.getSenderId())
                .userId2(friendRequest.getReceiverId())
                .createAt(LocalDateTime.now())
                .build();
        friendsRepository.save(friends);
    }

    // Cancel Friend Request
    public void refuseFriendRequest(RefuseFriendRequest request) {
        FriendRequest friendRequest = friendRequestRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        if (friendRequest.getStatus() != Status.PENDING) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        // Update status to REJECTED
        friendRequest.setStatus(Status.REJECTED);
        friendRequest.setUpdateAt(LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
    }

    // Delete Friend
    public void unfriend(String userId1, String userId2){
        Friends friendship = friendsRepository.findByUserId1AndUserId2(userId1, userId2)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        friendsRepository.delete(friendship);
    }

    // Friend List
    public List<String> getFriends(String userId) {
        return friendsRepository.findAllByUserId1OrUserId2(userId, userId)
                .stream()
                .map(friend -> friend.getUserId1().equals(userId) ? friend.getUserId2() : friend.getUserId1())
                .collect(Collectors.toList());
    }

    // Pending friend request list
    public List<FriendRequest> getPendingRequests(String receiverId) {
        return friendRequestRepository.findAllByReceiverIdAndStatus(receiverId, Status.PENDING);
    }


    // Check status for friendship
    public boolean areFriends(String userId1, String userId2){
        return friendsRepository.findAllByUserId1AndUserId2(userId1, userId2).isPresent()
                || friendsRepository.findAllByUserId1AndUserId2(userId2, userId1).isPresent();
    }

    // Rut lai yeu cau ket ban da tu choi
    public void resendFriendRequest(String senderId, String receiverId){
        FriendRequest request = friendRequestRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .orElse(null);

        if(request != null && request.getStatus() != Status.REJECTED){
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        // create new friend request
        FriendRequest newRequest = FriendRequest.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .status(Status.PENDING)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        friendRequestRepository.save(newRequest);
    }
}
