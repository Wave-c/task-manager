package com.wave.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wave.auth_service.dto.AuthResponse;
import com.wave.auth_service.dto.JwtDto;
import com.wave.auth_service.dto.UserDto;
import com.wave.auth_service.entity.User;
import com.wave.auth_service.service.AuthService;
import com.wave.auth_service.service.JwtService;
import com.wave.auth_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/jwt-controller")
@RequiredArgsConstructor
@Tag(name = "Работа с токенами")
public class JwtController 
{
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthService authenticationService;

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token)
    {
        UserDetails user = new User().setUsername(getUser(token).getUsername());
        return ResponseEntity.ok(jwtService.isTokenValid(token, user, true));
    }

    @GetMapping("/get-user")
    public User getUser(@RequestParam String token) 
    {
        User user = userService.getByUsername(jwtService.extractUserName(token, true));
        return user;
    }
    

    @Operation(summary = "Получение нового access токена")
    @GetMapping("/token")
    public ResponseEntity<?> getNewAccessToken(@CookieValue(value = "RefreshToken") String rt, @RequestParam String instance, HttpServletResponse response)
    {
        JwtDto jwt = authenticationService.getNewAccessToken(rt, instance);

        if(rt.isEmpty())
        {
            return ResponseEntity.status(401).build();
        }
        User user = getUser(jwt.getAccessToken());

        AuthResponse authResponse = new AuthResponse()
            .setAccessToken(jwt.getAccessToken())
            .setRefresh(jwt.getRefreshToken())
            .setUser(new UserDto().setUsername(user.getUsername()).setPassword(user.getPassword()))
            .setInstance(instance);

        Cookie cookieRefresh = new Cookie("RefreshToken", jwt.getRefreshToken());
        cookieRefresh.setPath("/");
        cookieRefresh.setMaxAge(30*24*60*60*1000);
        cookieRefresh.setHttpOnly(true);
        response.addCookie(cookieRefresh);
        response.setContentType("text/plain");
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Получение новой пары токенов")
    @GetMapping("/refresh")
    public ResponseEntity<?> getNewTokens(@CookieValue(value = "RefreshToken") String rt, @RequestParam String instance, HttpServletResponse response)
    {
        JwtDto jwt = authenticationService.getNewTokens(rt, instance);
        if(rt.isEmpty())
        {
            return ResponseEntity.status(401).build();
        }
        User user = getUser(jwt.getAccessToken());

        AuthResponse authResponse = new AuthResponse()
            .setAccessToken(jwt.getAccessToken())
            .setRefresh(jwt.getRefreshToken())
            .setUser(new UserDto().setUsername(user.getUsername()).setPassword(user.getPassword()))
            .setInstance(instance);

        Cookie cookieRefresh = new Cookie("RefreshToken", jwt.getRefreshToken());
        cookieRefresh.setPath("/");
        cookieRefresh.setMaxAge(30*24*60*60*1000);
        cookieRefresh.setHttpOnly(true);
        response.addCookie(cookieRefresh);
        response.setContentType("text/plain");

        return ResponseEntity.ok(authResponse);
    }
}
