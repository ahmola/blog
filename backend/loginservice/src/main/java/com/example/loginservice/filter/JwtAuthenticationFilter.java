package com.example.loginservice.filter;

import com.example.loginservice.user.UserRepository;
import com.example.loginservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 의존성 무한 루프 문제로 서비스가 아닌 레포지토리 사용
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 인증 정보를 가져옴
        String authHeader = request.getHeader("Authorization");

        // 헤더가 비어있지 않고 알맞은 인증 방식이라면
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            // 토큰 추출
            String token = authHeader.substring(7);

            try {
                // 토큰에서 유저 이름을 가져옴
                String username = jwtUtil.extractUsername(token);

                // 이름이 비어있지 않고 아직 인증되지 않았다면, 토큰 발급
                // 이름이 비었거나 인증이 이미 되어있다면 아무것도 할 필요가 없음
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    // 유저 이름으로 유저를 가져옴
                    UserDetails userDetails = userRepository.findByUsername(username).get();
                    // 토큰이 유효한지 검사
                    if(jwtUtil.validateToken(token, userDetails.getUsername())){
                        // 유효하면 토큰을 줌. 권한은 어차피 비어있고, 비밀번호는 비워둔다.
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails.getUsername(), null, null
                                );
                        // 컨텍스트에 유저가 인증되었다고 저장
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }catch (Exception e) {
                throw new RuntimeException("Invalid Token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
