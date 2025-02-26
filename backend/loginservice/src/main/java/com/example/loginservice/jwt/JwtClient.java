package com.example.loginservice.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "api-gateway")
public interface JwtClient {

    @PostMapping("/jwt/generateToken")
    ResponseEntity<String> generateToken(String username);

    @GetMapping("/jwt/extractUsername")
    ResponseEntity<String> extractUsername(
            @RequestHeader(name = "Authorization") String token
    );

    @GetMapping("/jwt/validateToken")
    ResponseEntity<Boolean> validateToken(
            @RequestHeader(name = "validateToken") String token
    );
}
