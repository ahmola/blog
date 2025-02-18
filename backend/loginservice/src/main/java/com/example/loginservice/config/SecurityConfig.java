package com.example.loginservice.config;

import com.example.loginservice.filter.JwtAuthenticationFilter;
import com.example.loginservice.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        // Data Access Object 인증 공급자 초기화
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // UserDetailsService 주입
        daoAuthenticationProvider.setUserDetailsService(userService);
        // PasswordEncoder 주입
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf().disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeHttpRequests(auth -> {
                            // 로그인 관련 기능은 열어둠
                            auth.requestMatchers("/auth/signin").permitAll();
                            auth.requestMatchers("/auth/signup").permitAll();
                            // 나머지 기능은 인증 요구
                            auth.anyRequest().authenticated();
                        }
                );

        http
                .addFilterBefore(   // jwt인증필터를 기본 인증 필터 앞에 추가함
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
