package com.example.user.post;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "post-service")
public interface PostClient {
}
