package com.example.apigateway.filter;

import com.example.apigateway.Error.JwtError;
import com.example.apigateway.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter implements GatewayFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 요청을 받음
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().getPath();

        // 로그인 요청에는 인증이 필요없음
        if(uri.contains("/auth/signin") || uri.contains("/auth/signup"))
            return chain.filter(exchange);


        boolean isContain = request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
        log.info("check authentication : " + isContain);
        // Authorization 헤더에서 JWT가 없으면 에러
        if (!isContain)
            return JwtError.onError(exchange, "Unauthorized", HttpStatus.UNAUTHORIZED);

        //토큰을 받음
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // Bearer 부분을 제거하고 순수 토큰만 추출
        token = token.replace("Bearer ", "");

        boolean isValidate = jwtService.validateToken(token);
        log.info("is token validate ? : " + isValidate);
        // 토큰이 만료되거나 오류가 없는지 확인하고 있다면 에러
        if(!isValidate)
            return JwtError.onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);

        // 검증된 토큰에서 자격 증명을 가져옴
        String username = jwtService.getUsernameFromToken(token);
        log.info(JWTAuthenticationFilter.class.toString() + " authenticated user : " + username);

        // Response 헤더에 X-User-Id 정보 추가
        // 이 헤더는 토큰이 검증되었다는 표시로 서비스들이 토큰을 따로 검증할 필요없이 나중에 이 헤더의 유무만 확인하면 된다.
        // 그러나 이는 만료된 토큰을 악용할 소지가 있으므로 나중에 Redis를 활용할 예정
        // 어차피 도커 네트워크에 묶일 것이기 때문에 굳이 사용할 이유는 없으므로 일단은 deprecated
        //        ServerHttpRequest modifiedRequest = request.mutate()
        //        .header("X-User-Id", username)
        //        .build();
        return chain.filter(exchange);
    }

    // 최우선 순위 필터로 지정
    @Override
    public int getOrder() {
        return -1;
    }
}
