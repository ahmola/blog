package com.example.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommentCacheScheduler {

    private final StringRedisTemplate redisTemplate;
    private final CommentRepository commentRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 60000) // 60초마다 실행
    public void flushCacheToDB() throws JsonProcessingException{
        Set<String> keys = redisTemplate.keys("comment:*");

        for (String key : keys){
            String value = redisTemplate.opsForValue().get(key);
            Comment comment = objectMapper.readValue(value, Comment.class);
            commentRepository.save(comment);

            redisTemplate.delete(key); // db로 flush 후 삭제
        }
    }
}
