package com.example.auth.auth;

import com.example.auth.jwt.JwtUtil;
import com.example.auth.role.Role;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService{
    private final AuthRepository authRepository;
    private final AuthMapper authMapper;

    @Transactional
    public Boolean save(AuthCreationRequest request){
        Auth auth = authMapper.CreationRequestToAuth(request);
        try {
            log.info(auth.toString());
            authRepository.save(auth);
        }catch (Exception e){
            throw new RuntimeException("Something goes wrong during Creating " + auth.toString());
        }
        return true;
    }

    public AuthResponse getAuthById(Long id){
        return authMapper.AuthToResponse(authRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Transactional
    public Boolean updateAuth(AuthUpdateRequest request){
        try {
            authRepository.save(authMapper.UpdateRequestToAuth(request));
        }catch (Exception e){
            throw new RuntimeException("Something goes wrong while Updating " + request);
        }
        return true;
    }

    @Transactional
    public Boolean deleteById(Long id){
        try {
            authRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("Error occurs during deleting");
        }
        return true;
    }

    public AuthResponse signIn(AuthRequest request) {
        Auth auth = authRepository.findByUsername(request.username())
                .orElseThrow();
        return authMapper.AuthToResponse(auth);
    }
}
