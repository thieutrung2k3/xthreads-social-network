package com.xthreads.auth_service.service;

import com.xthreads.auth_service.constant.RoleConstant;
import com.xthreads.auth_service.dto.request.*;
import com.xthreads.auth_service.dto.response.AccountResponse;
import com.xthreads.auth_service.dto.response.ApiResponse;
import com.xthreads.auth_service.dto.response.UserResponse;
import com.xthreads.auth_service.entity.Account;
import com.xthreads.auth_service.entity.Role;
import com.xthreads.auth_service.exception.AppException;
import com.xthreads.auth_service.exception.ErrorCode;
import com.xthreads.auth_service.mapper.AccountMapper;
import com.xthreads.auth_service.mapper.UserMapper;
import com.xthreads.auth_service.repository.AccountRepository;
import com.xthreads.auth_service.repository.RoleRepository;
import com.xthreads.auth_service.repository.client.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserClient userClient;
    UserMapper userMapper;

    @Value("${file.upload-dir}")
    @NonFinal
    String uploadDir;
    public ApiResponse<AccountResponse> registerAccount(MultipartFile file, AccountCreationRequest request){
        // Lưu ảnh và lấy đường dẫn
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            fileName = saveImage(file);
        }
        request.setUrlProfilePicture(fileName);
        Account account = accountMapper.toAccount(request);
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(RoleConstant.USER).ifPresent(roles::add);

        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRoles(roles);

        try{
            accountRepository.save(account);
        }catch (Exception e){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        UserCreationRequest userCreationRequest = userMapper.toUserCreationRequest(request);
        userCreationRequest.setAccountID(account.getId());

        userClient.registerAccount(userCreationRequest);

        return ApiResponse.<AccountResponse>builder()
                .result(accountMapper.toAccountResponse(account))
                .build();
    }

    public String saveImage(MultipartFile file){
        log.info(System.getProperty("user.dir") + uploadDir);
        if(file.isEmpty()){
            throw new AppException(ErrorCode.FILE_NOT_EXISTED);
        }
        try{
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + extension;
            Path path = Paths.get(uploadDir, newFileName);

            Files.createDirectories(path.getParent());
            file.transferTo(path.toFile());

            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ApiResponse<AccountResponse> updateAccount(String accountID, AccountUpdateRequest request){
        Account account = accountRepository.findById(accountID).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        if(passwordEncoder.matches(request.getOldPassword(), account.getPassword())){
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            return ApiResponse.<AccountResponse>builder()
                    .result(accountMapper.toAccountResponse(accountRepository.save(account)))
                    .build();
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    public UserResponse updateUserInformation(MultipartFile file, UserUpdateRequest request){
        String fileName = saveImage(file);

        request.setUrlProfilePicture(fileName);
        UserResponse response = userClient.updateInformationUser(request)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        return response;
    }

    public void updateUserAvatar(String accountID, MultipartFile file){
        String fileName = saveImage(file);
        log.info(fileName);
        UserAvatarUpdateRequest request = UserAvatarUpdateRequest.builder()
                .urlProfilePicture(fileName)
                .build();

            userClient.updateUserAvatar(accountID, request);

    }
}
