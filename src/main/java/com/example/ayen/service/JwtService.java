package com.example.ayen.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {

    private static final long EXPIRATIONTIME = 86400000; // 1일
    private static final String PREFIX = "Bearer ";
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 이메일로 JWT 생성
    public String getToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(KEY)  // KEY 그대로 사용 가능
                .compact();
    }

    // Authentication 객체로 JWT 생성
    public String generateToken(Authentication authentication) {
        String userEmail = authentication.getName();
        return getToken(userEmail);
    }

}
