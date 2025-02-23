package com.xthreads.post_service.service.consumer;

import com.xthreads.post_service.entity.Reaction;
import com.xthreads.post_service.mapper.ReactionMapper;
import com.xthreads.post_service.repository.ReactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactionConsumer {

    ReactionRepository reactionRepository;
    ReactionMapper reactionMapper;

    @KafkaListener(topics = "reaction-topic-v1", groupId = "post-service")
    public void addReaction(Reaction request){
        log.info(request.getReactionType().name());
        Optional<Reaction> existingReaction = reactionRepository.findByAccountIdAndPostId(request.getAccountId(), request.getPostId());

        if(existingReaction.isPresent()){
            Reaction reaction = existingReaction.get();
            if(request.getReactionType() == reaction.getReactionType()){
                reactionRepository.delete(reaction);
                //reactionQueueService.addToDeleteQueue(reaction);
                return;
            }

            reaction.setReactionType(request.getReactionType());
            reactionRepository.save(reaction);
            //reactionQueueService.addToQueue(reaction);
        }
        else{
            reactionRepository.save(request);
            //reactionQueueService.addToQueue(reactionMapper.toReaction(request));
        }
    }
}
