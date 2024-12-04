package com.xthreads.user_service.controller;

import com.xthreads.user_service.dto.request.UserCreationRequest;
import com.xthreads.user_service.dto.response.ApiResponse;
import com.xthreads.user_service.dto.response.UserResponse;
import com.xthreads.user_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-all-users")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return userService.getAllUsers();
    }

}
