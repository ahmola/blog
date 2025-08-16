package com.example.kafkaes;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostConsumer {

    private final PostRepository postRepository;
    private final PostDocumentRepository esRepository;

    @KafkaListener(topics = "post.created", groupId = "group_id")
    public void consume(@Payload Long postId){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Entity is not created from DB")
        );
        PostDocument doc = new PostDocument(post.getId(), post.getTitle(), post.getContent());
        esRepository.save(doc);
    }
}
