package com.example.gatewayservice;

import com.example.gatewayservice.config.JwtAthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAthenticationFilter jwtAthenticationFilter;

    @Bean
    public RouteLocator router(RouteLocatorBuilder builder){
        return builder.routes()
                .route("user-service", route-> route.path("/api/users/**")
                        .filters(f-> f.filter( jwtAthenticationFilter))
                        .uri("lb://user-service"))
                .route("user-service", route-> route.path("/api/auth/**")
                        .uri("lb://user-service"))
                .route("Task Service", route-> route.path("/api/tasks/**")
                        .filters(f-> f.filter( jwtAthenticationFilter))
                        .uri("lb://TaskService"))
                .build();
    }
}
