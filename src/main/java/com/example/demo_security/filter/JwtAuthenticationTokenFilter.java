package com.example.demo_security.filter;

import com.example.demo_security.bo.UserDetailsImpl;
import com.example.demo_security.config.JwtProperties;
import com.example.demo_security.utils.FastJwt;
import com.example.demo_security.utils.FastRedis;
import com.example.demo_security.utils.FastResponse;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final FastJwt fastJwt;
    private final FastRedis fastRedis;
    private final JwtProperties jwtProperties;
    private final FastResponse fastResponse;
    public JwtAuthenticationTokenFilter(FastJwt fastJwt, FastRedis fastRedis, JwtProperties jwtProperties, FastResponse fastResponse) {
        this.fastJwt = fastJwt;
        this.fastRedis = fastRedis;
        this.jwtProperties = jwtProperties;
        this.fastResponse = fastResponse;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            Claims currentClaims = fastJwt.parse(token);
            String jwt = (String) fastRedis.get(jwtProperties.jwtKey + currentClaims.getSubject());
            if (Objects.nonNull(jwt) && !Objects.equals(jwt, token)) {
                Claims redisClaims = fastJwt.parse(jwt);
                if (currentClaims.getIssuedAt().getTime() < redisClaims.getIssuedAt().getTime()) {
                    fastResponse.sendResponse(response, "登录已过期");
                    return;
                }
            }
            UserDetailsImpl userDetails = (UserDetailsImpl) fastRedis.get(jwtProperties.userKey + currentClaims.getSubject());
            if (Objects.isNull(userDetails)) {
                fastResponse.sendResponse(response, "登录已过期");
                return;
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    currentClaims.getSubject(), null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("JwtTokenFilter output Exception: " + e);
            fastResponse.sendResponse(response, e.getMessage());
        }
    }
}
