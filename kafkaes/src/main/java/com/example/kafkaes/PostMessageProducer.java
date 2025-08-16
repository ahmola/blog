package com.example.kafkaes;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostMessageProducer {
    private final KafkaTemplate<String, Long> kafkaTemplate;

    public void sendPostCreateEvent(Long postId){
        kafkaTemplate.send("post.created", postId);
    }
}
