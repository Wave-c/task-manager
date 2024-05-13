package com.wave.auth_service.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wave.auth_service.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService 
{
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final Map<String, String> refreshStorage = new HashMap<>();


    public String extractUserName(String token, boolean isJwt)
    {
        return extractClaim(token, Claims::getSubject, isJwt);
    }

    public String generateToken(UserDetails userDetails, boolean isJwt, String instance)
    {
        Map<String, Object> claims = new HashMap<>();

        if(userDetails instanceof User customUserDetails)
        {
            claims.put("id", customUserDetails.getId());
            claims.put("role", customUserDetails.getRole());
        }

        String token = generateToken(claims, userDetails, isJwt);
        if(!isJwt)
        {
            refreshStorage.put(userDetails.getUsername() + '/' + instance, token);
        }
        return token;
    }

    public Boolean isTokenValid(String token, UserDetails userDetails, boolean isJwt)
    {
        final String USERNAME = extractUserName(token, isJwt);
        return (USERNAME.equals(userDetails.getUsername())) && !isTokenExpired(token, isJwt);
    }

    public String removeRefreshToken(UserDetails userDetails, String instance)
    {
        return refreshStorage.remove(userDetails.getUsername() + "/" + instance);
    }

    public String getRefreshToken(UserDetails userDetails, String instance)
    {
        return refreshStorage.get(userDetails.getUsername() + "/" + instance);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers, boolean isJwt)
    {
        final Claims CLAIMS = extractAllClaims(token, isJwt);
        return claimsResolvers.apply(CLAIMS);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, boolean isJwt)
    {
        Date refreshExpiration = new Date();
        if(isJwt)
        {
            refreshExpiration = Date.from(LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault()).toInstant());
        }
        else
        {
            refreshExpiration = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        }

        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(refreshExpiration)
            .signWith( SignatureAlgorithm.HS256, getJwtSecretKey()).compact();
    }

    private boolean isTokenExpired(String token, boolean isJwt)
    {
        return extractExpiration(token, isJwt).before(new Date());
    }

    private Date extractExpiration(String token, boolean isJwt)
    {
        return extractClaim(token, Claims::getExpiration, isJwt);
    }

    private Claims extractAllClaims(String token, boolean isJwt)
    {
        return Jwts.parser().setSigningKey(getJwtSecretKey()).build().parseClaimsJws(token).getBody();
    }

    private SecretKey getJwtSecretKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}