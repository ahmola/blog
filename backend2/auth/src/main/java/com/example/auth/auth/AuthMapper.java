package com.example.auth.auth;

import com.example.auth.jwt.JwtUtil;
import com.example.auth.role.Role;
import com.example.auth.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AuthMapper {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;

    public AuthResponse AuthToResponse(Auth auth){
        Set<Role> roles = (Set<Role>) auth.getAuthorities();
        Set<String> roleNames = roles
                .stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());

        return new AuthResponse(
                auth.getId(),
                auth.getUsername(),
                roleNames,
                jwtUtil.generateToken(auth.getUsername())
        );
    }

    public Auth AuthRequestToAuth(AuthRequest request){
        return Auth.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }

    public Auth CreationRequestToAuth(AuthCreationRequest request){
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleRepository.findById(request.role())
                        .orElse(roleRepository.findByAuthority("user")));
        return Auth.builder()
                .roles(roles)
                .password(passwordEncoder.encode(request.password()))
                .username(request.username())
                .build();
    }

    public Auth UpdateRequestToAuth(AuthUpdateRequest request){
        return Auth.builder()
                .id(request.id())
                .roles((Set<Role>) roleRepository.findAllById(request.role()))
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }
}