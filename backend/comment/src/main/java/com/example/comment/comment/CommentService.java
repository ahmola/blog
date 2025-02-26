package com.example.comment.comment;

import com.example.comment.user.UserClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserClient userClient;

    public LocalDateTime formatter(String date){
        return LocalDateTime.parse(date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public CommentDTO convertToDTO(Comment comment){
        return CommentDTO.builder()
                .id(comment.getId())
                .subject(comment.getSubject())
                .createdAt(comment.getCreatedAt().toString())
                .username(comment.getUsername())
                .userId(comment.getUserId())
                .isUserNotBanned(comment.isUserNotBanned())
                .postId(comment.getPostId())
                .postTitle(comment.getPostTitle())
                .build();
    }

    public Comment convertDTOtoComment(CommentDTO commentDTO){
        return Comment.builder()
                .id(commentDTO.getId())
                .username(commentDTO.getUsername())
                .subject(commentDTO.getSubject())
                .userId(commentDTO.getUserId())
                .isUserNotBanned(commentDTO.isUserNotBanned())
                .postTitle(commentDTO.getPostTitle())
                .postId(commentDTO.getPostId())
                .createdAt(formatter(commentDTO.getCreatedAt()))
                .build();
    }

    public List<Comment> getAllCommentsByUserId(long userId) {
        return commentRepository.findByUserId(userId);
    }

    public List<Comment> getAllCommentsByPostId(long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public Comment save(CommentDTO commentDTO) {
        return commentRepository.save(convertDTOtoComment(commentDTO));
    }

    @Transactional
    public Comment fix(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getId()).get();
        try {
            comment = convertDTOtoComment(commentDTO);
        }catch (Exception e){
            throw new RuntimeException("Error occurs during updating comment : "
                    + commentDTO.getId());
        }
        return commentRepository.save(comment);
    }

    @Transactional
    public Boolean deleteById(long id) {
        try {
            commentRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("Error occurs during deleting comment " + id);
        }
        return true;
    }
}
