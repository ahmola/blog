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
public class CommentService {
    private final CommentRepository repository;

    @Cacheable(value = "comment", key = "#id")
    @Transactional(readOnly = true)
    public Comment getComment(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found " + id));
    }

    // 생성 후 즉시 캐시 적재, 객체의 id를 저장
    @CachePut(value = "comment", key = "#result.id")
    @Transactional
    public Comment createComment(Comment comment){
        return repository.save(comment);
    }

    // 키 무효화
    @CacheEvict(value = "comment", key="#id")
    @Transactional
    public void deleteComment(Long id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Comment not found : " + id);
        }
        repository.deleteById(id);
    }
}