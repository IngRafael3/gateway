package com.example.gatewayservice.config;

import com.example.gatewayservice.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtils jwtUtils;


    public JwtAthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(!exchange.getRequest().getPath().toString().startsWith("/api/auth")){
            if(token !=null && token.startsWith("Bearer ")){
                token = token.substring(7);
                try {
                    if(jwtUtils.validateToken(token)){
                        Claims claims = jwtUtils.parseClaims(token);
                        exchange.getRequest().mutate().header("username",claims.getSubject()).build();
                    }else{
                        return onError(exchange, "invalid token", HttpStatus.UNAUTHORIZED);
                    }
                }catch (Exception e){
                    return onError(exchange, "Jwt validation invalid token", HttpStatus.UNAUTHORIZED);
                }
            }else{
                return onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange,String err, HttpStatus httpStatus){
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    private List<GrantedAuthority> extraAuthoties(Claims claims){
        return new ArrayList<>();
    }
}
