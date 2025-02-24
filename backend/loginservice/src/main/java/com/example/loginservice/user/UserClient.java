package com.example.loginservice.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {
    @PostMapping("/user/userCreated")
    ResponseEntity<String> postRequestForCreateUserToUserService(
            @RequestHeader("Referer") String referer, // user-service가 auth-service에서 온 것인지 확인하는 용도
            @RequestBody UserCreateRequest userCreateRequest);

}
