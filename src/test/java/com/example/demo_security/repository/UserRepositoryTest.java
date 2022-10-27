package com.example.demo_security.repository;

import com.example.demo_security.po.Permission;
import com.example.demo_security.po.Role;
import com.example.demo_security.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@SpringBootTest
class UserRepositoryTest {

    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private PermissionRepository permissionRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    public void create() {
        Permission permission_user_add = new Permission().setId(1).setCode("user:add").setName("添加用户");
        Permission permission_user_del = new Permission().setId(2).setCode("user:del").setName("删除用户");
        Permission permission_user_sel = new Permission().setId(3).setCode("user:sel").setName("查询用户");
        Permission permission_user_mod = new Permission().setId(4).setCode("user:mod").setName("修改用户");


        List<Permission> permissions = permissionRepository.saveAll(List.of(permission_user_add, permission_user_del, permission_user_sel, permission_user_mod));
        System.out.println(permissions);

        Role role_admin = new Role().setId(1).setCode("admin").setName("超级管理员").setPermissions(Set.of(permission_user_add, permission_user_del, permission_user_sel, permission_user_mod));
        Role role_normal = new Role().setId(2).setCode("normal").setName("普通人").setPermissions(Set.of(permission_user_sel, permission_user_mod));
        Role role_none = new Role().setId(3).setCode("none").setName("仅查看").setPermissions(Set.of(permission_user_sel));

        List<Role> roles = roleRepository.saveAll(List.of(role_admin, role_normal, role_none));
        System.out.println(roles)
        ;
        User user_Linda = new User().setId(1).setPassword(passwordEncoder.encode("123456")).setUsername("Linda").setRoles(Set.of(role_admin));
        User user_Tom = new User().setId(2).setPassword(passwordEncoder.encode("123456")).setUsername("Tom").setRoles(Set.of(role_normal));
        User user_James = new User().setId(3).setPassword(passwordEncoder.encode("123456")).setUsername("James").setRoles(Set.of(role_none));

        List<User> users = userRepository.saveAll(List.of(user_Linda, user_Tom, user_James));
        System.out.println(users);
    }
}