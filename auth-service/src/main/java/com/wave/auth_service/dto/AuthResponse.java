package com.wave.auth_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AuthResponse 
{
    private String accessToken;
    private String refresh;
    private UserDto user;
    private String instance;
}
