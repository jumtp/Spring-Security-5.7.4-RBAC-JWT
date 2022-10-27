package com.example.demo_security.service;


import com.example.demo_security.bo.UserDetailsImpl;
import com.example.demo_security.config.JwtProperties;
import com.example.demo_security.po.User;
import com.example.demo_security.utils.FastJwt;
import com.example.demo_security.utils.FastRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final AuthenticationManager authenticationManager;
    private final FastJwt fastJwt;
    private final FastRedis fastRedis;
    private final JwtProperties jwtProperties;


    public UserService(AuthenticationManager authenticationManager, FastJwt fastJwt, FastRedis fastRedis, JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.fastJwt = fastJwt;
        this.fastRedis = fastRedis;
        this.jwtProperties = jwtProperties;
    }

    public String login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            log.warn("认证异常 : {}", e.toString());
            throw new RuntimeException(e);
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        log.info("Login User : {}", userDetails.getUser());
        String jwt = fastJwt.createJWT(userDetails.getUsername());
        fastRedis.set(jwtProperties.userKey + userDetails.getUsername(), userDetails);
        fastRedis.set(jwtProperties.jwtKey + userDetails.getUsername(), jwt);
        return jwt;
    }

    public String logOut() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        fastRedis.del(jwtProperties.userKey + username);
        fastRedis.del(jwtProperties.jwtKey + username);
        return "退出登录";
    }

}
