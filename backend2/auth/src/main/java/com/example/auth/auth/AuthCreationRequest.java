package com.example.auth.auth;

public record AuthCreationRequest(
        String username,
        String password,
        Long role
) {
}