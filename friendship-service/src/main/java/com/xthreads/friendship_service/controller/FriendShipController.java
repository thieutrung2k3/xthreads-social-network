package com.xthreads.friendship_service.controller;

import com.xthreads.friendship_service.dto.request.AcceptFriendRequest;
import com.xthreads.friendship_service.dto.request.RefuseFriendRequest;
import com.xthreads.friendship_service.dto.request.SendFriendRequest;
import com.xthreads.friendship_service.dto.response.ApiResponse;
import com.xthreads.friendship_service.model.FriendRequest;
import com.xthreads.friendship_service.service.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendShipController {
    private final FriendShipService friendShipService;

    @PostMapping("/send-request")
    public ResponseEntity<ApiResponse<Void>> sendFriendRequest(@RequestBody SendFriendRequest request) {
        friendShipService.sendFriendRequest(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Friend request sent successfully", null));
    }

    @PutMapping("/accept-request")
    public ResponseEntity<ApiResponse<Void>> acceptFriendRequest(@RequestBody AcceptFriendRequest request) {
        friendShipService.acceptFriendRequest(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Friend request accepted", null));
    }

    @PutMapping("/refuse-request")
    public ResponseEntity<ApiResponse<Void>> refuseFriendRequest(@RequestBody RefuseFriendRequest request) {
        friendShipService.refuseFriendRequest(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Friend request refused", null));
    }

    @DeleteMapping("/unfriend")
    public ResponseEntity<ApiResponse<Void>> unfriend(@RequestParam String userId1, @RequestParam String userId2) {
        friendShipService.unfriend(userId1, userId2);
        return ResponseEntity.ok(new ApiResponse<>(200, "Unfriended successfully", null));
    }

    @GetMapping("/friends")
    public ResponseEntity<ApiResponse<List<String>>> getFriends(@RequestParam String userId) {
        List<String> friends = friendShipService.getFriends(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Friend list retrieved successfully", friends));
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<ApiResponse<List<FriendRequest>>> getPendingRequests(@RequestParam String receiverId) {
        List<FriendRequest> pendingRequests = friendShipService.getPendingRequests(receiverId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Pending requests retrieved successfully", pendingRequests));
    }

    @GetMapping("/are-friends")
    public ResponseEntity<ApiResponse<Boolean>> areFriends(@RequestParam String userId1, @RequestParam String userId2) {
        boolean result = friendShipService.areFriends(userId1, userId2);
        return ResponseEntity.ok(new ApiResponse<>(200, "Friendship status retrieved successfully", result));
    }

    @PostMapping("/resend-request")
    public ResponseEntity<ApiResponse<Void>> resendFriendRequest(@RequestParam String senderId, @RequestParam String receiverId) {
        friendShipService.resendFriendRequest(senderId, receiverId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Friend request resent successfully", null));
    }
}
