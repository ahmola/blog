package com.example.comment.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentDTO {

    private Long id;

    private String subject;

    private String createdAt;

    private Long userId;

    private String username;

    private boolean isUserNotBanned;

    private Long postId;

    private String postTitle;
}
