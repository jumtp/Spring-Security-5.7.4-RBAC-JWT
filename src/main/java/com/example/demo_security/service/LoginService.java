package com.example.demo_security.service;

import com.example.demo_security.bo.UserDetailsImpl;
import com.example.demo_security.po.User;
import com.example.demo_security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(LoginService.class);
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> loginUser = userRepository.findByUsername(username);
        User user = loginUser.orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
        log.info("Login User : {} , role : {} , permission : {}",
                user.getUsername(), user.getRoles(),
                user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).collect(Collectors.toSet())
        );
        return new UserDetailsImpl(user);
    }
}
