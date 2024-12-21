package com.xthreads.post_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xthreads.post_service.dto.request.PostCreationRequest;
import com.xthreads.post_service.dto.request.PostUpdateRequest;
import com.xthreads.post_service.dto.response.ApiResponse;
import com.xthreads.post_service.dto.response.PostResponse;
import com.xthreads.post_service.exception.AppException;
import com.xthreads.post_service.exception.ErrorCode;
import com.xthreads.post_service.repository.client.AuthClient;
import com.xthreads.post_service.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    PostService postService;
    ObjectMapper objectMapper;
    AuthClient authClient;

    @PostMapping("/create")
    public ApiResponse<PostResponse> createPost(@RequestPart(value = "file", required = false) MultipartFile file,
                                                @RequestPart("data") String data,
                                                @RequestHeader("Authorization") String token) throws JsonProcessingException {
        PostCreationRequest request = objectMapper.readValue(data, PostCreationRequest.class);

        ApiResponse<String> apiResponse = authClient.getAccountIdFromToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String accountID = apiResponse.getResult();
        log.info("Account ID: " + accountID);
        request.setAccountID(accountID);

        if (file != null) {
            return postService.createPost(file, request);
        } else {
            return postService.createPostWithoutFile(request);
        }
    }


    @GetMapping("/info")
    public ApiResponse<Page<PostResponse>> getPosts(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size){
        log.info("ahfuhefuheufheuhu");
        return postService.getPosts(page, size);
    }

    @GetMapping("/get-all")
    public ApiResponse<List<PostResponse>> getAllPost(){
        return postService.getAllPost();
    }
    @GetMapping("/get/{accountID}")
    public ApiResponse<List<PostResponse>> getAllPostByAccountID(@PathVariable String accountID){
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getAllPostByAccountID(accountID))
                .build();
    }

    @PutMapping("/update/{postID}")
    public ApiResponse<Void> updatePost(@RequestHeader("Authorization") String token,
                                        @PathVariable String postID,
                                        @RequestBody PostUpdateRequest request){
        ApiResponse<String> apiResponse = authClient.getAccountIdFromToken(token).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String accountID = apiResponse.getResult();
        return postService.updatePost(accountID, postID, request);
    }

    @DeleteMapping("/delete/{postID}")
    public ApiResponse<Void> deletePost(@RequestHeader("Authorization") String token,
                                        @PathVariable String postID){
        ApiResponse<String> apiResponse = authClient.getAccountIdFromToken(token).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String accountID = apiResponse.getResult();
        return postService.deletePost(accountID, postID);
    }
}
