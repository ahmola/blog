package com.example.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    // 토큰이 유효한지 검사
    public boolean validateToken(String token){
        try {
            // 만료되지 않았다면
            return extractClaims(token).getExpiration().before(new Date());
        }catch (Exception e){
            // 토큰 내용을 검출하는 도중 오류가 발생하면
            return false;
        }
    }



    public Claims extractClaims(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8)) // 키 설정
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            throw new RuntimeException("Token Cannot be extracted");
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }
}
