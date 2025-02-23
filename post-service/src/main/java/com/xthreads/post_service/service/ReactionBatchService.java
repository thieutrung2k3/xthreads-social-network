package com.xthreads.post_service.service;

import com.xthreads.post_service.entity.Reaction;
import com.xthreads.post_service.repository.ReactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactionBatchService {
    ReactionQueueService reactionQueueService;
    ReactionRepository reactionRepository;

    @Scheduled(fixedRate = 5000)
    public void processBatchReactions(){
        Queue<Reaction> reactionQueue = reactionQueueService.getReactionQueue();
        Queue<Reaction> deleteReactionQueue = reactionQueueService.getDeleteQueue();

        List<Reaction> batch = new ArrayList<>();
        List<Reaction> deleteBatch = new ArrayList<>();

        while(!reactionQueue.isEmpty() && batch.size() < 100){
            batch.add(reactionQueue.poll());
        }

        while(!deleteReactionQueue.isEmpty() && deleteBatch.size() < 100){
            deleteBatch.add(deleteReactionQueue.poll());
        }

        if(!deleteBatch.isEmpty()){
            reactionRepository.deleteAll(deleteBatch);
            log.info("Deleted " + deleteBatch.size() + " to reaction DB successfully.");
        }

        if(!batch.isEmpty()){
            reactionRepository.saveAll(batch);
            log.info("Saved " + batch.size() + " to reaction DB successfully.");
        }
    }
}
