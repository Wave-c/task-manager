package com.wave.auth_service.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wave.auth_service.entity.User;
import com.wave.auth_service.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-db")
@RequiredArgsConstructor
@Tag(name = "Работа с User Table для тестов")
public class UserTableController 
{
    private final UserService userService;


    @GetMapping("/all")
    public Iterable<User> allUsers()
    {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete-all")
    public String deleteAll()
    {
        userService.deleteAllUsers();
        return "OK";
    }
}