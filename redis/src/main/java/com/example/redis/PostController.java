package com.example.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@RestController
public class PostController {

    private final PostService service;

    @GetMapping
    public ResponseEntity<Post> getPost(@RequestParam(name = "id")Long id){
        return ResponseEntity.ok(service.getPost(id));
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post){
        log.info(post.toString());
        return new ResponseEntity<>(service.createPost(post), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Post> fixPost(@RequestBody Post post){
        return ResponseEntity.ok(service.createPost(post));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deletePost(@RequestParam(name = "id")Long id){
        service.deletePost(id);
        return ResponseEntity.ok(Boolean.TRUE);
    }
}
