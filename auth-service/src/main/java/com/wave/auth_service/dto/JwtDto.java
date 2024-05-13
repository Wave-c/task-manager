package com.wave.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDto 
{
    private String accessToken;
    private String refreshToken;
}
