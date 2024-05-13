package com.wave.auth_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wave.auth_service.dto.AuthResponse;
import com.wave.auth_service.dto.JwtDto;
import com.wave.auth_service.dto.LogoutDto;
import com.wave.auth_service.dto.UserDto;
import com.wave.auth_service.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController 
{
    private final AuthService authenticationService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserDto request, HttpServletResponse response) 
    {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        String instance = UUID.randomUUID().toString();
        JwtDto jwt = authenticationService.signUp(request, instance);

        AuthResponse authResponse = new AuthResponse()
            .setAccessToken(jwt.getAccessToken())
            .setRefresh(jwt.getRefreshToken())
            .setUser(request)
            .setInstance(instance);

        Cookie cookieRefresh = new Cookie("RefreshToken", jwt.getRefreshToken());
        cookieRefresh.setPath("/");
        cookieRefresh.setMaxAge(30*24*60*60*1000);
        cookieRefresh.setHttpOnly(true);
        response.addCookie(cookieRefresh);
        response.setContentType("text/plain");

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody @Valid UserDto request, HttpServletResponse response) 
    {
        String instance = UUID.randomUUID().toString();
        JwtDto jwt = authenticationService.signIn(request, instance);
        
        AuthResponse authResponse = new AuthResponse()
            .setAccessToken(jwt.getAccessToken())
            .setRefresh(jwt.getRefreshToken())
            .setUser(request)
            .setInstance(instance);

        Cookie cookieRefresh = new Cookie("RefreshToken", jwt.getRefreshToken());
        cookieRefresh.setPath("/");
        cookieRefresh.setMaxAge(30*24*60*60*1000);
        cookieRefresh.setHttpOnly(true);
        response.addCookie(cookieRefresh);
        response.setContentType("text/plain");

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Выход из аккаунта")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDto request) 
    {
        authenticationService.logout(request.getUser(), request.getInstance());
        return ResponseEntity.ok().build();
    }
    
}