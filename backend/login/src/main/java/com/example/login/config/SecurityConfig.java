package com.example.login.config;

import com.example.login.user.CustomUserDetailService;
import com.example.login.utils.JwtAuthenticateFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private JwtAuthenticateFilter jwtAuthenticateFilter;
    private CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        // 데이터 접근 객체 인증 제공자 초기화
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 디테일서비스 의존성 주입
        provider.setUserDetailsService(customUserDetailService);
        // 패스워드 암호화를 위한 인코더 주입
        provider.setPasswordEncoder(passwordEncoder());
        // 인증 제공자 매니저에 인증 제공자를 저장
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeHttpRequests(auth -> {
                    // 로그인 관련 기능에는 접근 가능
                    auth.requestMatchers("/login/**").permitAll();
                    // 나머지는 인증 요구
                    auth.anyRequest().authenticated();
                });

        http.
                addFilterBefore(jwtAuthenticateFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
