package com.xthreads.post_service.controller;

import com.xthreads.post_service.dto.request.ReactionCreationRequest;
import com.xthreads.post_service.dto.response.ApiResponse;
import com.xthreads.post_service.service.ReactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/reaction")
public class ReactionController {

    private static final Logger log = LoggerFactory.getLogger(ReactionController.class);
    ReactionService reactionService;

    @PostMapping()
    public ApiResponse<Void> addReaction(@RequestBody ReactionCreationRequest request){
        reactionService.addReaction(request);
        return ApiResponse.<Void>builder().build();
    }
}
