package com.wave.auth_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wave.auth_service.dto.JwtDto;
import com.wave.auth_service.dto.UserDto;
import com.wave.auth_service.entity.User;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService 
{
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtDto signUp(UserDto userDto, String instance)
    {
        User user = userService.create(userDto);

        String jwt = jwtService.generateToken(user, true, null);
        String rt = jwtService.generateToken(user, false, instance);
        return new JwtDto(jwt, rt);
    }

    public JwtDto signIn(UserDto userDto, String instance)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userDto.getUsername());
        
        String jwt = jwtService.generateToken(userDetails, true, null);
        String rt = jwtService.generateToken(userDetails, false, instance);

        return new JwtDto(jwt, rt);
    }

    public JwtDto getNewAccessToken(@NotNull String refreshToken, String instance)
    {
        String username = jwtService.extractUserName(refreshToken, false);
        UserDetails userDetails = userService.getByUsername(username);
        if(jwtService.isTokenValid(refreshToken, userDetails, false))
        {
            String savedRefreshToken = jwtService.getRefreshToken(userDetails, instance);
            if(!StringUtils.isEmpty(savedRefreshToken) && savedRefreshToken.equals(refreshToken))
            {
                String jwt = jwtService.generateToken(userDetails, true, null);
                return new JwtDto(jwt, savedRefreshToken);
            }
        }
        return new JwtDto(null, null);
    }

    public JwtDto getNewTokens(@NotNull String refreshToken, String instance)
    {
        String username = jwtService.extractUserName(refreshToken, false);
        UserDetails userDetails = userService.getByUsername(username);
        if(jwtService.isTokenValid(refreshToken, userDetails, false))
        {
            String savedRefreshToken = jwtService.removeRefreshToken(userDetails, instance);
            if(!StringUtils.isEmpty(savedRefreshToken) && savedRefreshToken.equals(refreshToken))
            {
                String access = jwtService.generateToken(userDetails, true, null);
                String refresh = jwtService.generateToken(userDetails, false, instance);
                return new JwtDto(access, refresh);
            }
        }
        return new JwtDto(null, null);
    }

    public void logout(UserDto user, String instance)
    {
        UserDetails userDetails = userService.getByUsername(user.getUsername());
        jwtService.removeRefreshToken(userDetails, instance);
    }
}