package com.example.post.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {
    @Deprecated
    @GetMapping("/user/isHeaderValidate")
    ResponseEntity<Boolean> isXUserIdHeaderValidate(
            @RequestHeader("Referer") String referer
            ,@RequestParam String username);

    @GetMapping("/user")
    ResponseEntity<String> getUserInfo(@RequestParam long userId);

    @GetMapping("/user/validateUser")
    ResponseEntity<Boolean> isUserNotBanned(@RequestParam long userId);
}
