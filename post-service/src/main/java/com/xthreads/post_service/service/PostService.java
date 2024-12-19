package com.xthreads.post_service.service;

import ch.qos.logback.core.util.StringUtil;
import com.xthreads.post_service.dto.request.PostCreationRequest;
import com.xthreads.post_service.dto.request.PostUpdateRequest;
import com.xthreads.post_service.dto.response.ApiResponse;
import com.xthreads.post_service.dto.response.PostResponse;
import com.xthreads.post_service.entity.Post;
import com.xthreads.post_service.exception.AppException;
import com.xthreads.post_service.exception.ErrorCode;
import com.xthreads.post_service.mapper.PostMapper;
import com.xthreads.post_service.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    PostRepository postRepository;
    PostMapper postMapper;

    @Value("${file.upload-dir}")
    @NonFinal
    String uploadDir;

    public ApiResponse<PostResponse> createPost(MultipartFile file, PostCreationRequest request){
        if(StringUtil.isNullOrEmpty(request.getAccountID()))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        String fileName = null;
        if(file != null && !file.isEmpty())
            fileName = saveImage(file);

        Post post = postMapper.toPost(request);
        post.setImageUrl(fileName);
        post.setCreatedAt(LocalDate.now());
        try{
            post = postRepository.save(post);
        }catch (Exception e){

        }
        return ApiResponse.<PostResponse>builder()
                .result(postMapper.toPostResponse(post))
                .build();
    }
    public ApiResponse<PostResponse> createPostWithoutFile(PostCreationRequest request){
        if(StringUtil.isNullOrEmpty(request.getAccountID()))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        Post post = postMapper.toPost(request);
        post.setCreatedAt(LocalDate.now());
        try{
            post = postRepository.save(post);
        }catch (Exception e){

        }
        return ApiResponse.<PostResponse>builder()
                .result(postMapper.toPostResponse(post))
                .build();
    }


    public ApiResponse<Page<PostResponse>> getPosts(int page, int size){
        log.info("service");
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> postResponses = postRepository.findAll(pageable).map(postMapper::toPostResponse);
        return ApiResponse.<Page<PostResponse>>builder()
                .result(postResponses)
                .build();
    }

    public ApiResponse<List<PostResponse>> getAllPost(){
        List<PostResponse> posts = postRepository.findAll().stream().map(postMapper::toPostResponse).collect(Collectors.toList());
        return ApiResponse.<List<PostResponse>>builder()
                .result(posts)
                .build();
    }
    public List<PostResponse> getAllPostByAccountID(String accountID){
        List<Post> posts = postRepository.findAllByAccountID(accountID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return posts.stream().map(postMapper::toPostResponse).collect(Collectors.toList());
    }

    public ApiResponse<Void> updatePost(String accountID, String postID, PostUpdateRequest request){
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        if(!accountID.equals(post.getAccountID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        postMapper.updatePost(post, request);
        post.setUpdatedAt(LocalDate.now());

        try{
            postRepository.save(post);
        }catch (Exception e){
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }

        return ApiResponse.<Void>builder()
                .message("Post has been updated successfully.")
                .build();
    }

    public ApiResponse<Void> deletePost(String accountID, String postID){
        if(!isUserPost(accountID, postID))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        try{
            postRepository.deleteById(postID);
        }catch (Exception e) {
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }
        return ApiResponse.<Void>builder()
                .message("Post has been deleted successfully.")
                .build();
    }

    public boolean isUserPost(String accountID, String postID){
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        if(accountID.equals(post.getAccountID())){
            return true;
        }
        return false;
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
        }catch (IOException e) {
            throw new AppException(ErrorCode.FILE_NOT_EXISTED);
        }
    }
}
