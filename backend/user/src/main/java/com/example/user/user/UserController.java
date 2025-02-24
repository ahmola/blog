package com.example.user.user;

import com.example.user.interceptor.Services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    // auth service에서 유저를 신규 등록 시 auth service가 유저를 추가할 것을 요청
    @PostMapping("/userCreated")
    public ResponseEntity<String> createUser(
            @RequestHeader(value = "Referer", required = true) String referer,
            @RequestBody UserDTO userDTO){

        // auth service에서 보내온게 아니라면 거부함
        // 오직 auth service에서만 유저 생성을 요청할 수 있음
        if (!referer.contains(Services.AUTH.toString()))
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        log.info(UserController.class.toString() + " user created : " + userDTO.getUsername());
        return new ResponseEntity<>(userService.save(userDTO).toString(), HttpStatus.CREATED);
    }

    // 각 서비스들에서 X-User-Id 인증 헤더가 유효한지 검사해줌
    @GetMapping("/isHeaderValidate")
    public ResponseEntity<Boolean> isXUserIdHeaderValidate(
            @RequestHeader("Referer") String referer,
            @RequestParam String username){

        // 서비스들 중 하나가 보낸 요청인지 확인
        if(Arrays.stream(Services.values())
                .map(Services::name)
                .anyMatch(referer::contains)){
            // 유효한 유저인지 확인
            if (userService.findByUsername(username).equals(null))
                return new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);

            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        }
        // 서비스에서 보낸 요청이 아니라면 거절
        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserInfo(@RequestParam long userId) {
        return new ResponseEntity<>(userService.getUserByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/userUpdate")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long userId){
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);
    }
}
