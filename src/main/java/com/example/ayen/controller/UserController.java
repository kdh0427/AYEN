package com.example.ayen.controller;

import com.example.ayen.dto.entity.User;
import com.example.ayen.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 생성자 주입
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");

        if (kakaoAccount == null) {
            return ResponseEntity.badRequest().body("kakao_account not found");
        }

        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            return ResponseEntity.badRequest().body("Email not found in kakao_account");
        }

        // 핵심 로직은 서비스로 위임
        return userService.getUserByEmail(email);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");

        if (kakaoAccount == null) {
            return ResponseEntity.badRequest().body("kakao_account not found");
        }

        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            return ResponseEntity.badRequest().body("Email not found in kakao_account");
        }

        return userService.getUserProfileByEmail(email);
    }

    @GetMapping("/rankings")
    public List<User> getTopUsers() {
        return userService.getTop10UsersByAchievementCount();
    }
}
