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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private String extractEmail(OAuth2User oAuth2User, Authentication authentication) {
        if (!(authentication instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken)) {
            return null;
        }

        String registrationId = ((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication)
                .getAuthorizedClientRegistrationId();

        if ("kakao".equalsIgnoreCase(registrationId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount != null) {
                return (String) kakaoAccount.get("email");
            }
        } else if ("google".equalsIgnoreCase(registrationId)) {
            return oAuth2User.getAttribute("email");
        }

        return null;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = extractEmail(oAuth2User, authentication);

        if (email == null) {
            return ResponseEntity.badRequest().body("Email not found or unsupported social login provider");
        }

        return userService.getUserByEmail(email);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = extractEmail(oAuth2User, authentication);

        if (email == null) {
            return ResponseEntity.badRequest().body("Email not found or unsupported social login provider");
        }

        return userService.getUserProfileByEmail(email);
    }

    @GetMapping("/rankings")
    public ResponseEntity<?> getTopUsers(Authentication authentication) {
        String email = null;
        Long userId = null;  // 로그인한 사용자 ID

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            email = extractEmail(oAuth2User, authentication);

            if (email != null) {
                Optional<User> optionalUser = userService.findUserByEmail(email);
                if (optionalUser.isPresent()) {
                    userId = optionalUser.get().getId();
                }
            }
        }

        List<User> rankings = userService.getTop10UsersByAchievementCount();

        Map<String, Object> response = new HashMap<>();
        response.put("currentUserEmail", email);
        response.put("currentUserId", userId);
        response.put("rankings", rankings);

        return ResponseEntity.ok(response);
    }


}
