package com.example.apigateway.Error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtError {

    public static Mono<Void> onError(ServerWebExchange exchange, String arr, HttpStatus status){
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}
