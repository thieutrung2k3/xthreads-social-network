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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendShipService {
    final FriendRequestRepository friendRequestRepository;
    final FriendsRepository friendsRepository;
    final FriendRequestValidator validator;

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
}
