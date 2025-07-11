package com.example.ayen.controller;

import com.example.ayen.dto.entity.Ending;
import com.example.ayen.dto.entity.User;
import com.example.ayen.dto.entity.UserEnding;
import com.example.ayen.dto.response.ApiResponse;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.ayen.dto.response.ChoiceRequest;
import com.example.ayen.dto.response.EndingDetailResponse;
import com.example.ayen.dto.response.UserEndingResponse;
import com.example.ayen.service.EndingService;
import com.example.ayen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/endings")
public class EndingController {

    private final UserService uService;
    private final EndingService endingService;

    public EndingController(UserService uService, EndingService endingService) {
        this.uService = uService;
        this.endingService = endingService;
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
    public ApiResponse<List<UserEndingResponse>> getEndingsByCurrentUser(Authentication authentication) {
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

        List<UserEnding> endings = endingService.findByUser_Id(user.getId());

        List<UserEndingResponse> responseList = endings.stream()
                .map(ua -> new UserEndingResponse(
                        ua.getEnding().getId(),
                        ua.getEnding().getTitle(),
                        ua.getEnding().getImage_url(),
                        ua.getAchievedAt()
                ))
                .collect(Collectors.toList());
         return new ApiResponse<>(200, responseList);
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<EndingDetailResponse> getEndingDetail(@PathVariable Long id, Authentication authentication) {
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

        Ending end = endingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Ending not found"));

        int cnt = endingService.countByUserIdAndEndingId(user.getId(), id);

        // UserEnding에서 해당 Ending에 해당하는 것만 조회 (필요시)
        Optional<UserEnding> userEndingOpt = endingService.findByUser_Id(user.getId()).stream()
                .filter(ua -> ua.getEnding().getId().equals(id))
                .findFirst();

        // 달성 일자 설정 (달성 기록이 있으면 그 날짜, 없으면 null)
        LocalDateTime achievedAt = userEndingOpt.map(UserEnding::getAchievedAt).orElse(null);

        EndingDetailResponse response = new EndingDetailResponse(
                end.getTitle(),
                end.getImage_url(),
                end.getDescription(),
                achievedAt,
                end.getExp(),
                cnt
        );

        return new ApiResponse<>(200, response);
    }
}
