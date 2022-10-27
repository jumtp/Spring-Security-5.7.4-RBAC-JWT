package com.example.demo_security.utils;


import com.example.demo_security.config.JwtProperties;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class FastJwt {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final JwtProperties jwtProperties;

    public FastJwt(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

    }

    public String createJWT(String subject) {

        return Jwts.builder()
                .setId(UUID.randomUUID().toString().replaceAll("-", ""))
                .setSubject(subject)
                .setIssuedAt(new java.util.Date())
//                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(60 * 5).toInstant(ZoneOffset.ofHours(8))))
                .signWith(signatureAlgorithm, jwtProperties.secret).compact();
    }

    public Claims parse(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser().setSigningKey(jwtProperties.secret).parseClaimsJws(jwt)
                .getBody();
    }

}
