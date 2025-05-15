package com.example.ayen.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    static final long EXPIRATIONTIME = 86400000;
    static final String PREFIX = "Bearer";

    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getToken(String useremail) {
        String token = Jwts.builder().setSubject(useremail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(key).compact();

        return token;
    }

    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token != null) {
            String user = Jwts.builder()
                    .signWith(key)
                    .compact();

            if(user != null)
                return user;
        }

        return null;
    }
}
