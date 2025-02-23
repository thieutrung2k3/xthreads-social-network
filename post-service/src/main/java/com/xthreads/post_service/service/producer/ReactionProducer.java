package com.xthreads.post_service.service.producer;

import com.xthreads.post_service.entity.Reaction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReactionProducer {
    KafkaTemplate<String, Reaction> kafkaTemplate;
    String TOPIC_REACTION = "reaction-topic-v1";

    public void sendReaction(Reaction reaction){
        reaction.setTimestamp(LocalDate.now());
        kafkaTemplate.send(TOPIC_REACTION, reaction);
    }

    public void sendDeleteReaction(String reactionId){
        kafkaTemplate.send(TOPIC_REACTION, Reaction.builder()
                        .reactionId(reactionId)
                        .build());
    }
}
