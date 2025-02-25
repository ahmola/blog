package com.example.apigateway.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/jwt")
@RestController
public class JwtController {

    private final JwtService jwtService;

    // 토큰 생성

    // 토큰에서 유저이름 추출

    // 토큰 유효성 검사

}
