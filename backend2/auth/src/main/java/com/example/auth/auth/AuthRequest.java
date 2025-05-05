package com.example.auth.auth;

public record AuthRequest(
        String username,
        String password
) {
}
