package com.example.loginservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)   // 유저 이름
                .setIssuedAt(new Date(System.currentTimeMillis()))    // 생성일
                .setExpiration(new Date(System.currentTimeMillis()+expiration)) // 만료일
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)  // 서명, 위조 방지용
                .compact(); // 토큰 생성
    }

    // 토큰 내 정보(payload 또는 body)를 claim이라고 함
    public Claims extractClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())) // 키 설정
                    .build()
                    .parseClaimsJws(token) // 추출할 토큰
                    .getBody(); // payload 추출
        }catch (Exception e){
            throw new RuntimeException("Something goes wrong during extracting claims");
        }
    }

    // 만료되었는지 확인
    public boolean isNotExpired(String token){
        return extractClaims(token).getExpiration().after(new Date(System.currentTimeMillis()));
    }

    // 유저 이름 추출
    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    // 토큰이 유효한지 검사
    public boolean validateToken(String token, String username){
        try{
            return extractUsername(token).equals(username) && isNotExpired(token);
        }catch (Exception e){
            throw new RuntimeException("Invalid Token");
        }
    }
}
