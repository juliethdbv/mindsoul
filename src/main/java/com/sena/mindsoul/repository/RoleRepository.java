package com.sena.mindsoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.mindsoul.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
