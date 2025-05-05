package com.example.auth.auth;

import java.util.Set;

public record AuthUpdateRequest(
        Long id,
        String username,
        String password,
        Set<Long> role
) {
}
