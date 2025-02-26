package com.example.post.post;

import com.example.post.user.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostController {
    private final PostService postService;
    private final UserClient userClient;

    private boolean isValidateUser(long userId){
        return userClient.isUserNotBanned(userId).getBody();
    }

    // 유저의 모든 게시물 GET
    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam long userId){
        return new ResponseEntity<>(postService.getAllPostsByUserId(userId), HttpStatus.OK);
    }

    // 유저의 게시물을 가져옴
    @GetMapping
    public ResponseEntity<String> getPost(@RequestParam long postId){
        return new ResponseEntity<>(postService.getPostById(postId).get().toString(), HttpStatus.OK);
    }

    // 유저의 게시물 생성
    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostDTO postDTO){
        if (isValidateUser(postDTO.getUserId()))
            return new ResponseEntity<>(postService.save(postDTO).toString(), HttpStatus.CREATED);
        return new ResponseEntity<>("User is Banned", HttpStatus.FORBIDDEN);
    }

    // 게시물 수정
    @PutMapping
    public ResponseEntity<String> fixPost(@RequestBody PostDTO postDTO){
        return new ResponseEntity<>(postService.fixPost(postDTO).toString(), HttpStatus.OK);
    }

    // 게시물 삭제
    @DeleteMapping
    public ResponseEntity<Boolean> removePost(@RequestParam long postId){
        return new ResponseEntity<>(postService.deleteById(postId), HttpStatus.OK);
    }
}
