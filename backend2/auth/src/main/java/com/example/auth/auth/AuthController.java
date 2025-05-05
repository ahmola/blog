package com.example.auth.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(
            @RequestBody @Valid AuthRequest request){
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> singUp(
            @RequestBody AuthCreationRequest request){
        return ResponseEntity.ok(authService.save(request));
    }

    @GetMapping
    public ResponseEntity<AuthResponse> getAuthById(
            @RequestParam Long id
    ){
        return ResponseEntity.ok(authService.getAuthById(id));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateAuth(
            @RequestBody @Valid AuthUpdateRequest request){
        return ResponseEntity.ok(authService.updateAuth(request));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAuthById(
            @RequestParam Long id){
        return ResponseEntity.ok(authService.deleteById(id));
    }
}
