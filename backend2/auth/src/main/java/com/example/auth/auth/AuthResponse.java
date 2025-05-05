package com.example.auth.auth;

import java.util.Set;

public record AuthResponse(
        Long id,
        String username,
        Set<String> role,
        String token
) {
}
