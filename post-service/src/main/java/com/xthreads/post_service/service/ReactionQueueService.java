package com.xthreads.post_service.service;

import com.xthreads.post_service.dto.request.ReactionCreationRequest;
import com.xthreads.post_service.entity.Reaction;
import com.xthreads.post_service.mapper.ReactionMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactionQueueService {
    Queue<Reaction> reactionQueue = new ConcurrentLinkedQueue<>();
    Queue<Reaction> deleteQueue = new ConcurrentLinkedQueue<>();

    ReactionMapper reactionMapper;

    public void addToQueue(Reaction reaction){
        reactionQueue.add(reaction);
    }

    public void addToDeleteQueue(Reaction reaction){
        deleteQueue.add(reaction);
    }

    public Queue<Reaction> getDeleteQueue() {
        return deleteQueue;
    }

    public Queue<Reaction> getReactionQueue(){
        return reactionQueue;
    }
}
