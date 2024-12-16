package com.xthreads.auth_service.service;

import com.xthreads.auth_service.constant.RoleConstant;
import com.xthreads.auth_service.dto.request.AccountCreationRequest;
import com.xthreads.auth_service.dto.request.UserCreationRequest;
import com.xthreads.auth_service.dto.response.AccountResponse;
import com.xthreads.auth_service.dto.response.ApiResponse;
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
}
