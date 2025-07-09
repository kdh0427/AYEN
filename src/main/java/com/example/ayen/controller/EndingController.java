package com.example.ayen.controller;

import com.example.ayen.dto.entity.User;
import com.example.ayen.dto.entity.UserEnding;
import com.example.ayen.dto.response.ChoiceRequest;
import com.example.ayen.dto.response.UserEndingResponse;
import com.example.ayen.service.EndingService;
import com.example.ayen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/endings")
    public List<UserEndingResponse> getEndingsByCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            throw new RuntimeException("인증된 사용자가 아닙니다.");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = extractEmail(oAuth2User, authentication);

        if (email == null) {
            throw new RuntimeException("이메일 정보를 가져올 수 없습니다.");
        }

        User user = uService.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<UserEnding> endings = eService.findByUser_Id(user.getId());

        return endings.stream()
                .map(ua -> new UserEndingResponse(
                        ua.getEnding().getId(),
                        ua.getEnding().getTitle(),
                        ua.getEnding().getImage_url(),
                        ua.getAchievedAt()
                ))
                .collect(Collectors.toList());
    }
}
