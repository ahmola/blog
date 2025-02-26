package com.example.user.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (!referer.contains("AUTH"))
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        log.info("user created : " + userDTO.getUsername());
        return new ResponseEntity<>(
                userService.save(userDTO).toString(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> getUserInfo(@RequestParam long userId) {
        return new ResponseEntity<>(
                userService.getUserByUserId(userId).toString(), HttpStatus.OK);
    }

    @GetMapping("/validateUser")
    public ResponseEntity<Boolean> isUserNotBanned(@RequestParam long userId){
        return new ResponseEntity<>(
                userService.getUserByUserId(userId).isNotBanned() , HttpStatus.OK
        );
    }

    @PutMapping("/userUpdate")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(
                userService.updateUser(userDTO).toString(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(@RequestParam Long userId){
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);
    }
}
