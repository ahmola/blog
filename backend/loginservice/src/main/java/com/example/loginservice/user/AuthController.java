package com.example.loginservice.user;

import com.example.loginservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserService userService;
    // 의존성 무한 루프로 서비스가 아닌 컨트롤러에서 암호화 진행
    private final PasswordEncoder passwordEncoder;

    private final UserClient userClient;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody UserDTO userDTO){
        // 유저 생성
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // user-service에 유저 생성 요청
        UserCreateRequest userCreateRequest = new UserCreateRequest(userDTO.getUsername(), "USER");
        userClient.postRequestForCreateUserToUserService("AUTH", userCreateRequest);

        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody UserDTO userDTO){
        // 토큰 발급
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(),  userDTO.getPassword()));
        return new ResponseEntity<>(jwtUtil.generateToken(userDTO.getUsername()), HttpStatus.OK);
    }

    // test
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("test success", HttpStatus.OK);
    }
}
