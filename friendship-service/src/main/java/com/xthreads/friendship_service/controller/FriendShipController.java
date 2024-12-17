package com.xthreads.friendship_service.controller;

import com.xthreads.friendship_service.dto.request.AcceptFriendRequest;
import com.xthreads.friendship_service.dto.request.RefuseFriendRequest;
import com.xthreads.friendship_service.dto.request.SendFriendRequest;
import com.xthreads.friendship_service.dto.response.ApiResponse;
import com.xthreads.friendship_service.service.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
