package com.example.rdb;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }


    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no such user : " + id)
        );
    }
}
