package com.example.user.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User DTOtoUser(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        Optional.ofNullable(userDTO.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(userDTO.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(userDTO.getRole()).map(Role::valueOf).ifPresent(user::setRole);
        Optional.ofNullable(userDTO.isNotBanned()).ifPresent(user::setNotBanned);

        return user;
    }

    @Transactional
    public User save(UserDTO userDTO) {
        return userRepository.save(DTOtoUser(userDTO));
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional(rollbackOn = Exception.class)
    public User updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).get();
        try {
            user = DTOtoUser(userDTO);
        }catch (Exception e){
            throw new RuntimeException("Error occurs during updating user "
                    + userDTO.getUsername());
        }
        return userRepository.save(user);
    }

    @Transactional(rollbackOn = Exception.class)
    public Boolean deleteUserById(Long userId) {
        try {
            userRepository.deleteById(userId);
        }catch (Exception e){
            throw new RuntimeException("Error Occurs during Deleting User : " + userId);
        }
        return true;
    }

    public User findByUsername(String username) {
        User user;
        try {
            user = userRepository.findByUsername(username).get();
        }catch (Exception e){
            throw new RuntimeException("Cannot find User : " + username);
        }
        return user;
    }
}
