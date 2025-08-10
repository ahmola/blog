package com.example.redis;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository repository;

    @Cacheable(value = "post", key = "#id")
    @Transactional(readOnly = true)
    public Post getPost(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found " + id));
    }

    // 생성 후 즉시 캐시 적재, 객체의 id를 저장
    @CachePut(value = "post", key = "#result.id")
    @Transactional
    public Post createPost(Post post){
        return repository.save(post);
    }

    // 키 무효화
    @CacheEvict(value = "post", key="#id")
    @Transactional
    public void deletePost(Long id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Post not found : " + id);
        }
        repository.deleteById(id);
    }
}