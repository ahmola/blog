package com.example.post.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user/isHeaderValidate")
    ResponseEntity<Boolean> isXUserIdHeaderValidate(
            @RequestHeader("Referer") String referer
            ,@RequestParam String username);

}
