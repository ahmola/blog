package com.example.comment.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
@RestController
public class CommentController {

    private final CommentService commentService;

    // 유저의 모든 댓글 가져오기
    @GetMapping("/all")
    public ResponseEntity<List<Comment>> getAllCommentsByUserId(@RequestParam long userId){
        return new ResponseEntity<>(
                commentService.getAllCommentsByUserId(userId), HttpStatus.OK);
    }

    // 게시물의 모든 댓글 가져오기
    public ResponseEntity<List<Comment>> getAllCommentsByPostId(@RequestParam long postId){
        return new ResponseEntity<>(
                commentService.getAllCommentsByPostId(postId), HttpStatus.OK);
    }

    // 댓글 추가
    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CommentDTO commentDTO){
        if(commentDTO.isUserNotBanned())
            return new ResponseEntity<>(commentService.save(commentDTO).toString(),
                    HttpStatus.CREATED);
        return new ResponseEntity<>("User is banned", HttpStatus.FORBIDDEN);
    }

    @PutMapping
    public ResponseEntity<String> fixComment(@RequestBody CommentDTO commentDTO){
        return new ResponseEntity<>(commentService.fix(commentDTO).toString(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> removeComment(@RequestParam long id){
        return new ResponseEntity<>(commentService.deleteById(id), HttpStatus.OK);
    }
}
