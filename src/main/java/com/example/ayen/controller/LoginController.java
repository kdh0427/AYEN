package com.example.ayen.controller;

import com.example.ayen.service.JwtService;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginController(JwtService jwtService,
                           AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
/*
    // 로컬 로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountCredentials credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body("Login 성공");
    }
    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoCallback(OAuth2AuthenticationToken auth) {
        return "http://localhost:3000/sinario";
    }
*/
}
