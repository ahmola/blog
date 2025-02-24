package com.example.jwt.gateway;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "api-gateway")
public interface GatewayClient {
}
