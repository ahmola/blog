package com.example.apigateway.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/jwt")
@RestController
public class JwtController {

    private final JwtService jwtService;

    private String headerToToken(ServerHttpRequest request){
        String token = Objects.requireNonNull(
                        request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .replace("Bearer ", "");
        log.info("get token : " + token);
        return token;
    }

    // 토큰 생성
    @PostMapping("/generateToken")
    public Mono<ResponseEntity<String>> generateToken(String username){
        log.info("generate token of : " + username);
        return Mono.just(
                new ResponseEntity<>(
                        jwtService.generateToken(username), HttpStatus.CREATED));
    }

    // 토큰에서 유저이름 추출
    @GetMapping("/extractUsername")
    public Mono<ResponseEntity<String>> extractUsername(ServerHttpRequest request){
        String token = headerToToken(request);
        return Mono.just(
                new ResponseEntity<>(
                        jwtService.extractUsername(token), HttpStatus.OK));
    }

    // 토큰 유효성 검사
    @GetMapping("/validateToken")
    public Mono<ResponseEntity<Boolean>> validateToken(ServerHttpRequest request){
        String token = headerToToken(request);
        return Mono.just(
                new ResponseEntity<>(
                        jwtService.validateToken(token), HttpStatus.OK));
    }
}
