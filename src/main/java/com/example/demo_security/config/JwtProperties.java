package com.example.demo_security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {
    @Value("${jwt.secret}")
    public String secret;

    @Value("${jwt.redis-key.user}")
    public String userKey;

    @Value("${jwt.redis-key.jwt}")
    public String jwtKey;


}
