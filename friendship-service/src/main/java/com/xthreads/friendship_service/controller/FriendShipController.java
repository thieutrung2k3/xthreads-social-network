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
    public ApiResponse<Void> sendFriendRequest(@RequestBody SendFriendRequest request) {
        friendShipService.sendFriendRequest(request);
        return new ApiResponse<>(200, "Friend request sent successfully", null);
    }

    @PutMapping("/accept-request")
    public ApiResponse<Void> acceptFriendRequest(@RequestBody AcceptFriendRequest request) {
        friendShipService.acceptFriendRequest(request);
        return new ApiResponse<>(200, "Friend request accepted", null);
    }

    @PutMapping("/refuse-request")
    public ApiResponse<Void> refuseFriendRequest(@RequestBody RefuseFriendRequest request) {
        friendShipService.refuseFriendRequest(request);
        return new ApiResponse<>(200, "Friend request refused", null);
    }

    @DeleteMapping("/unfriend")
    public ApiResponse<Void> unfriend(@RequestParam String userId1, @RequestParam String userId2) {
        friendShipService.unfriend(userId1, userId2);
        return new ApiResponse<>(200, "Unfriended successfully", null);
    }

    @GetMapping("/friends")
    public ApiResponse<List<String>> getFriends(@RequestParam String userId) {
        List<String> friends = friendShipService.getFriends(userId);
        return new ApiResponse<>(200, "Friend list retrieved successfully", friends);  // Trả về ApiResponse với danh sách bạn bè
    }

    @GetMapping("/pending-requests")
    public ApiResponse<List<FriendRequest>> getPendingRequests(@RequestParam String receiverId) {
        List<FriendRequest> pendingRequests = friendShipService.getPendingRequests(receiverId);
        return new ApiResponse<>(200, "Pending requests retrieved successfully", pendingRequests);  // Trả về ApiResponse với danh sách yêu cầu bạn bè
    }

    @GetMapping("/are-friends")
    public ApiResponse<Boolean> areFriends(@RequestParam String userId1, @RequestParam String userId2) {
        boolean result = friendShipService.areFriends(userId1, userId2);
        return new ApiResponse<>(200, "Friendship status retrieved successfully", result);  // Trả về ApiResponse với kết quả Boolean
    }

    @PostMapping("/resend-request")
    public ApiResponse<Void> resendFriendRequest(@RequestParam String senderId, @RequestParam String receiverId) {
        friendShipService.resendFriendRequest(senderId, receiverId);
        return new ApiResponse<>(200, "Friend request resent successfully", null);  // Trả về ApiResponse trực tiếp
    }

}
