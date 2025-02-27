package com.example.loginservice.user;

import com.example.loginservice.jwt.JwtClient;
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

    private final JwtClient jwtClient;

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
        return new ResponseEntity<>(
                jwtClient.generateToken(userDTO.getUsername()).getBody(), HttpStatus.OK);
    }

    // user-service에서 유저 삭제 시 같이 cascade
    @DeleteMapping
    public ResponseEntity<Boolean> delete(@RequestParam long userId){
        return new ResponseEntity<>(
                userService.deleteById(userId), HttpStatus.OK
        );
    }

    // test
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("test success", HttpStatus.OK);
    }
}
