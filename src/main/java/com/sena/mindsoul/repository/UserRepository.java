package com.sena.mindsoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.mindsoul.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
