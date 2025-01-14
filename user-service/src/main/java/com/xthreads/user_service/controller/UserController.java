package com.xthreads.user_service.controller;

import com.xthreads.user_service.dto.request.UserAvatarUpdateRequest;
import com.xthreads.user_service.dto.request.UserCreationRequest;
import com.xthreads.user_service.dto.request.UserUpdateRequest;
import com.xthreads.user_service.dto.response.ApiResponse;
import com.xthreads.user_service.dto.response.UserResponse;
import com.xthreads.user_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/information")
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> registerUser(@RequestBody UserCreationRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("/get/{accountID}")
    public ApiResponse<UserResponse> getUserByAccountID(@PathVariable String accountID){
        return userService.getUserByAccountID(accountID);
    }

    @GetMapping("/get-all-users")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update/user/{accountID}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String accountID, @RequestBody UserUpdateRequest request){
        return userService.updateUser(accountID, request);
    }

    @PutMapping("/update/avatar/{accountID}")
    public void updateUserAvatar(@PathVariable String accountID, @RequestBody UserAvatarUpdateRequest request){
        userService.updateUserAvatar(accountID, request);
    }
}
