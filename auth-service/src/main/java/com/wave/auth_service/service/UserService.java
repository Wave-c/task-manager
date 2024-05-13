package com.wave.auth_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wave.auth_service.dto.UserDto;
import com.wave.auth_service.entity.Role;
import com.wave.auth_service.entity.User;
import com.wave.auth_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService 
{
    private final UserRepository userRepository;

    public User save(UserDto userDto)
    {
        User user = new User()
            .setUsername(userDto.getUsername())
            .setPassword(userDto.getPassword())
            .setId(UUID.randomUUID().toString())
            .setCreatedAt(LocalDateTime.now())
            .setRole(Role.USER_ROLE);
        
        return userRepository.save(user);
    }

    public User save(User user)
    {
        return userRepository.save(user);
    }

    public User create(UserDto user)
    {
        if(userRepository.existsByUsername(user.getUsername()))
        {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        return save(user);
    }

    public User getById(String id)
    {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким id не найден"));
    }

    public User getByUsername(String username)
    {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким Username не найден"));
    }

    public UserDetailsService userDetailsService()
    {
        return this::getByUsername;
    }

    public User getCurrentUser()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public Iterable<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public void deleteAllUsers()
    {
        userRepository.deleteAll();
    }
}