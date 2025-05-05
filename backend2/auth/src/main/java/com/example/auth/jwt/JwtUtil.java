package com.example.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final String secret = "thisisthesupersecretthisisthesupersecretthisisthesupersecretthisisthesupersecretthisisthesupersecretthisisthesupersecret";

    private final long expiration = 3600000;

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username){
        log.info("generate token with : " + username);

        try {
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignKey())
                    .compact();
        }catch (Exception e){
            throw new RuntimeException("Error occurs during generating token " + e.getMessage());
        }
    }

    public Claims extractClaims(String token){
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (Exception e){
            throw new RuntimeException("Error occur during extracting claims "+ e.getMessage());
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Boolean validateToken(String token){
        Claims claims = extractClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()))
                && !claims.getSubject().isEmpty();
    }
}
