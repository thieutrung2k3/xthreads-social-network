package com.xthreads.post_service.service;

import com.xthreads.post_service.dto.request.ReactionCreationRequest;
import com.xthreads.post_service.dto.response.ReactionResponse;
import com.xthreads.post_service.entity.Reaction;
import com.xthreads.post_service.exception.AppException;
import com.xthreads.post_service.exception.ErrorCode;
import com.xthreads.post_service.mapper.ReactionMapper;
import com.xthreads.post_service.repository.ReactionRepository;
import com.xthreads.post_service.service.producer.ReactionProducer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactionService {
    private static final Logger log = LoggerFactory.getLogger(ReactionService.class);
    ReactionRepository reactionRepository;
    ReactionMapper reactionMapper;
    ReactionProducer reactionProducer;

    public void addReaction(ReactionCreationRequest request){

        reactionProducer.sendReaction(reactionMapper.toReaction(request));
    }

    public List<Reaction> getReactionByPost(String postId){
        return reactionRepository.findAllByPostId(postId);
    }




}
