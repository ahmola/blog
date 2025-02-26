package com.example.comment.post;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "post-service")
public interface PostClient {
}
