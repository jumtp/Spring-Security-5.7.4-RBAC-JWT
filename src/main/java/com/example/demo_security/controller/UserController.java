package com.example.demo_security.controller;


import com.example.demo_security.po.User;
import com.example.demo_security.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        return userService.login(user);
    }
    @GetMapping("/logout")
    public String logout(){
        return userService.logOut();
    }

    @GetMapping("/current")
    public Object current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("user", authentication.getName());
        map.put("authorities", authorities);
        return map;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('user:add')")
    public String add() {
        return "You Can add";
    }

    @GetMapping("/del")
    @PreAuthorize("hasAuthority('user:del')")
    public String del() {
        return "You Can del";
    }

    @GetMapping("/sel")
    @PreAuthorize("hasAuthority('user:sel')")
    public String sel() {
        return "You Can sel";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasAuthority('user:mod')")
    public String mod() {
        return "You Can mod";
    }


    @GetMapping("/role")
    @PreAuthorize("hasRole('admin')")
    public String role() {
        return "you are admin";
    }

}
