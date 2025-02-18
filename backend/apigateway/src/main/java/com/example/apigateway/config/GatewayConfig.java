package com.example.apigateway.config;

import com.example.apigateway.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class GatewayConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // 로그인 서비스 라우팅
                .route("login-service",
                        r -> r.path("/auth/**")
                                .uri("lb://login-service"))
                // 유저 서비스 라우팅
                .route("user-service",
                        r -> r.path("/user/**")
                                .filters(f -> f.filter(jwtAuthenticationFilter))
                                .uri("lb://user-service"))
                // 게시물 서비스 라우팅
                .route("post-service",
                        r -> r.path("/post/**")
                                .filters(f -> f.filter(jwtAuthenticationFilter))
                                .uri("lb://post-service"))
                // 댓글 서비스 라우팅
                .route("comment-service",
                        r -> r.path("/comment/**")
                                .filters(f -> f.filter(jwtAuthenticationFilter))
                                .uri("lb://comment-service"))
                .build();
    }
}
