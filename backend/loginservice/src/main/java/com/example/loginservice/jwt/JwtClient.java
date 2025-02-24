package com.example.loginservice.jwt;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "jwt-service")
public interface JwtClient {
}
