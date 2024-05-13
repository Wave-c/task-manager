package com.wave.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wave.auth_service.entity.User;

public interface UserRepository extends JpaRepository<User, String>
{
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}