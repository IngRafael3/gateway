package com.example.gatewayservice;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator router(RouteLocatorBuilder builder){
        return builder.routes()
                .route("user-service", route-> route.path("/api/users/**")
                        .uri("lb://user-service"))
                .route("Task Service", route-> route.path("/api/tasks/**")
                        .uri("lb://TaskService"))
                .build();
    }
}
