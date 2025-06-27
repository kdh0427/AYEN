package com.example.ayen.controller;

import com.example.ayen.dto.entity.User;
import com.example.ayen.dto.entity.UserEnding;
import com.example.ayen.dto.response.AchievementResponse;
import com.example.ayen.dto.response.UserEndingResponse;
import com.example.ayen.service.EndingService;
import com.example.ayen.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class EndingController {

    private final UserService uService;
    private final EndingService eService;

    public EndingController(UserService uService, EndingService eService) {
        this.uService = uService;
        this.eService = eService;
    }

    @GetMapping("/endings")
    public List<UserEndingResponse> getEndingsByCurrentUser(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");

        if (kakaoAccount == null) {
            throw new RuntimeException("kakao_account not found");
        }

        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            throw new RuntimeException("Email not found in kakao_account");
        }

        User user = uService.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<UserEnding> Endings = eService.findByUser_Id(user.getId());

        return Endings.stream()
                .map(ua -> new UserEndingResponse(
                        ua.getEnding().getId(),
                        ua.getEnding().getTitle(),
                        ua.getEnding().getImage_url(),
                        ua.getAchievedAt()// 실제 필드명에 맞게 변경
                ))
                .collect(Collectors.toList());
    }
}
