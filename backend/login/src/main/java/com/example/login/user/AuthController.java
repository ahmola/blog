package com.example.login.user;

import com.example.login.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private CustomUserDetailService customUserDetailService;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        // 유저의 암호를 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // db에 저장
        return new ResponseEntity<>(
                customUserDetailService.save(user).getUsername(), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody User user) {
        return new ResponseEntity<>()
    }
}
