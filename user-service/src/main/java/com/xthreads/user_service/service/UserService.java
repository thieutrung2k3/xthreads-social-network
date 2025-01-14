package com.xthreads.user_service.service;

import com.xthreads.user_service.dto.request.UserAvatarUpdateRequest;
import com.xthreads.user_service.dto.request.UserCreationRequest;
import com.xthreads.user_service.dto.request.UserUpdateRequest;
import com.xthreads.user_service.dto.response.ApiResponse;
import com.xthreads.user_service.dto.response.UserResponse;
import com.xthreads.user_service.entity.User;
import com.xthreads.user_service.exception.AppException;
import com.xthreads.user_service.exception.ErrorCode;
import com.xthreads.user_service.mapper.UserMapper;
import com.xthreads.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public ApiResponse<UserResponse> registerUser(UserCreationRequest request){
        User user = userMapper.toUser(request);
        try{
            user  = userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return ApiResponse.<UserResponse>builder()
                .result(userMapper.toUserResponse(user))
                .build();
    }

    public ApiResponse<List<UserResponse>> getAllUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList()))
                .build();
    }

    public ApiResponse<UserResponse> getUserByAccountID(String accountID){
        User user = userRepository.findByAccountID(accountID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return ApiResponse.<UserResponse>builder()
                .result(userMapper.toUserResponse(user))
                .build();
    }

    public ApiResponse<UserResponse> updateUser(String accountID, UserUpdateRequest request){
        User user = userRepository.findByAccountID(accountID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);

        return ApiResponse.<UserResponse>builder()
                .result(userMapper.toUserResponse(userRepository.save(user)))
                .build();
    }

    public ApiResponse<Void> deleteUser(String id){
        try{
            userRepository.deleteById(id);
            return ApiResponse.<Void>builder()
                    .message("User has been deleted.")
                    .build();
        }catch (Exception e){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    public void updateUserAvatar(String id, UserAvatarUpdateRequest request){
        User user = userRepository.findByAccountID(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setUrlProfilePicture(request.getUrlProfilePicture());
        try{
            userRepository.save(user);
        }catch (Exception e){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

}
