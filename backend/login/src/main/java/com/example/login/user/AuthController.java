package com.example.login.user;

import com.example.login.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/login")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        // 유저의 암호를 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // db에 저장
        return new ResponseEntity<>(
                customUserDetailService.save(user).getUsername(), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()));
        return new ResponseEntity<>(jwtUtils.generateToken(user.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/showall")
    public ResponseEntity<List<User>> showall(){
        return new ResponseEntity<>(customUserDetailService.showall(), HttpStatus.OK);
    }
}
