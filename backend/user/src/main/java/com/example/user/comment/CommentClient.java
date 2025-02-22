package com.example.user.comment;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "comment-service")
public interface CommentClient {
}