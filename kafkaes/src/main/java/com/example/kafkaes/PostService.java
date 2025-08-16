package com.example.kafkaes;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostDocumentRepository esRepository;
    private final PostMessageProducer postMessageProducer;

    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No Such User : " + id));
    }

    public Post createPost(Post post){
        Post saved = postRepository.save(post);
        postMessageProducer.sendPostCreateEvent(saved.getId());
        return saved;
    }

    public Post getPost(Long id) {
        try {
            PostDocument doc = esRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Elastic Search Miss.")
            );
            log.info("Elastic Search Hit. Return {}", id);
            return new Post(doc.getId(), doc.getTitle(), doc.getContent());
        }catch (Exception e){
            log.info("{} Fallback to RDB", e.getMessage());
            return postRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("No such user : " + id)
            );
        }
    }
}
