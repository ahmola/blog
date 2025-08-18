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
public class CommentController {

    private final CommentService service;

    @GetMapping
    public ResponseEntity<Comment> getPost(@RequestParam(name = "id")Long id){
        return ResponseEntity.ok(service.getComment(id));
    }

    @PostMapping
    public ResponseEntity<Comment> createPost(@RequestBody Comment comment){
        log.info(comment.toString());
        return new ResponseEntity<>(service.createComment(comment), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Comment> fixPost(@RequestBody Comment comment){
        return ResponseEntity.ok(service.createComment(comment));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deletePost(@RequestParam(name = "id")Long id){
        service.deleteComment(id);
        return ResponseEntity.ok(Boolean.TRUE);
    }
}
