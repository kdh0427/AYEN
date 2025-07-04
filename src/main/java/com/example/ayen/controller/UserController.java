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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = null;
        if (authentication instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) {
            registrationId = ((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        }

        String email = null;

        if ("kakao".equalsIgnoreCase(registrationId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount == null) {
                return ResponseEntity.badRequest().body("kakao_account not found");
            }
            email = (String) kakaoAccount.get("email");
            if (email == null) {
                return ResponseEntity.badRequest().body("Email not found in kakao_account");
            }
        } else if ("google".equalsIgnoreCase(registrationId)) {
            email = oAuth2User.getAttribute("email");
            if (email == null) {
                return ResponseEntity.badRequest().body("Email not found in Google user attributes");
            }
        } else {
            return ResponseEntity.badRequest().body("Unsupported social login provider");
        }

        return userService.getUserByEmail(email);
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // registrationId는 SecurityContext에서 꺼내거나, OAuth2AuthenticationToken에서 직접 가져올 수 있음
        String registrationId = null;
        if (authentication instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) {
            registrationId = ((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        }

        String email = null;

        if ("kakao".equalsIgnoreCase(registrationId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount == null) {
                return ResponseEntity.badRequest().body("kakao_account not found");
            }
            email = (String) kakaoAccount.get("email");
            if (email == null) {
                return ResponseEntity.badRequest().body("Email not found in kakao_account");
            }
        } else if ("google".equalsIgnoreCase(registrationId)) {
            // 구글은 최상위 속성에 email이 있음
            email = oAuth2User.getAttribute("email");
            if (email == null) {
                return ResponseEntity.badRequest().body("Email not found in Google user attributes");
            }
        } else {
            return ResponseEntity.badRequest().body("Unsupported social login provider");
        }

        return userService.getUserProfileByEmail(email);
    }

    @GetMapping("/rankings")
    public List<User> getTopUsers() {
        return userService.getTop10UsersByAchievementCount();
    }
}
