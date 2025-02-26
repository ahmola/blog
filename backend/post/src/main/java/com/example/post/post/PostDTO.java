package com.example.post.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDTO {

    private Long id;

    private String title;

    private String subject;

    private Integer like;

    private String localDateTime;

    private Long userId;

    private String username;
}
