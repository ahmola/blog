package com.example.login.utils;

import com.example.login.user.CustomUserDetailService;
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
public class JwtAuthenticateFilter extends OncePerRequestFilter { // securityfilterchain에 적용

    private JwtUtils jwtUtils;
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 정보를 가져옴
        String authHeader = request.getHeader("Authorization");
        // 헤더가 비어있지 않고 Bearer 로 시작하는지, 즉 토큰이 있는지 확인
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String username = jwtUtils.extractUsername(token);

                // 이름이 비어있지 않고 아직 인증되지 않았다면
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 이름을 통해 유저 정보를 가져오고
                    UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                    // 토큰이 유효한지 검사
                    if(jwtUtils.validateToken(token, userDetails.getUsername())) {
                        // 유효하면 토큰을 리턴함. 이 때 토큰에는 비밀번호가 저장되어서는 안됨
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        // securitycontext에 토큰을 발급
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }catch (Exception e){
                throw new RuntimeException("Invalid Token");
            }
        }
        // security filter chain에 필터에 추가
        filterChain.doFilter(request, response);
    }
}
