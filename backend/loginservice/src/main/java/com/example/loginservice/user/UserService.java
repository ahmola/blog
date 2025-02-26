package com.example.loginservice.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    // 중간에 끊기지 않게 트랜잭션 처리를 한다.
    @Transactional
    public User save(User user){
        try {
            return userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException("Something went wrong during saving user " + user.getUsername());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 유저를 찾아보고 없다면 에러
        UserDetails userDetails = userRepository.findByUsername(username).orElseThrow( () -> {
            throw new UsernameNotFoundException("There is no such user " + username);
        });
        return userDetails;
    }
}
