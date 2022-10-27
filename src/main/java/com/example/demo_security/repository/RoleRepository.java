package com.example.demo_security.repository;

import com.example.demo_security.po.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
