package com.example.demo_security.bo;

import com.example.demo_security.po.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDetailsImpl implements UserDetails {

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public UserDetailsImpl setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Stream<SimpleGrantedAuthority> roles = user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getCode()));
        Stream<SimpleGrantedAuthority> permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(permission -> new SimpleGrantedAuthority(permission.getCode()));
        return Stream.concat(roles, permissions).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
