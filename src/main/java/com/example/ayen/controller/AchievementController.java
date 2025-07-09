package com.example.ayen.controller;

import com.example.ayen.dto.response.AchievementResponse;
import com.example.ayen.dto.entity.User;
import com.example.ayen.dto.entity.UserAchievement;
import com.example.ayen.dto.response.ApiResponse;
import com.example.ayen.service.AchievementService;
import com.example.ayen.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private final UserService userService;
    private final AchievementService aService;

    public AchievementController(UserService userService,AchievementService aService) {
        this.userService = userService;
        this.aService = aService;
    }

    private String extractEmail(OAuth2User oAuth2User, Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            return null;
        }

        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        if ("kakao".equalsIgnoreCase(registrationId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        } else if ("google".equalsIgnoreCase(registrationId)) {
            return oAuth2User.getAttribute("email");
        }

        return null;
    }

    @GetMapping("/me")
    public ApiResponse<List<AchievementResponse>> getAchievementsByCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            throw new RuntimeException("인증된 사용자가 아닙니다.");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = extractEmail(oAuth2User, authentication);

        if (email == null) {
            throw new RuntimeException("이메일 정보를 가져올 수 없습니다.");
        }

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<UserAchievement> userAchievements = aService.findByUser_Id(user.getId());

        List<AchievementResponse> responseList = userAchievements.stream()
                .map(ua -> new AchievementResponse(
                        ua.getAchievement().getTitle(),
                        ua.getAchievement().getImage_url(),
                        ua.getAchievedAt()
                ))
                .collect(Collectors.toList());

        return new ApiResponse<>(200, responseList);
    }
}
