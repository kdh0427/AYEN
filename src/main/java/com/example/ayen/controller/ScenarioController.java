package com.example.ayen.controller;

import com.example.ayen.dto.entity.Scenario;
import com.example.ayen.dto.response.ApiResponse;
import com.example.ayen.dto.response.ChoiceRequest;
import com.example.ayen.dto.response.UserScene;
import com.example.ayen.service.ChoiceService;
import com.example.ayen.service.ScenarioService;
import com.example.ayen.service.SceneService;
import com.example.ayen.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class ScenarioController {
    private final SceneService sceneService;
    private final ScenarioService scenarioService;
    private final UserService userService;
    private final ChoiceService choiceService;

    public ScenarioController(SceneService sceneService, ScenarioService scenarioService, UserService userService, ChoiceService choiceService) {
        this.sceneService = sceneService;
        this.scenarioService = scenarioService;
        this.userService = userService;
        this.choiceService = choiceService;
    }

    private String extractEmail(OAuth2User oAuth2User, String registrationId) {
        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        } else if ("google".equals(registrationId)) {
            return oAuth2User.getAttribute("email");
        }
        return null; // 다른 provider도 추가 가능
    }

    @GetMapping("/scenarios")
    public Iterable<Scenario> findScenarios() {
        return scenarioService.getAllScenarios();
    }

    @PostMapping("/scenarios/{scenarioId}/scenes")
    public ResponseEntity<Map<String, Object>> getLastSceneId(
            @PathVariable Long scenarioId,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        try {
            // ✅ 카카오 사용자 정보에서 이메일 추출
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            String email = extractEmail(oAuth2User, registrationId);

            if (email == null) {
                response.put("error", registrationId + " 계정 이메일 정보 없음");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // ✅ 이메일로 사용자 ID 조회
            Long userId = userService.findIdByEmail(email);

            // ✅ 사용자 ID와 시나리오 ID로 마지막 Scene ID 조회
            Long lastSceneId = sceneService.findLastSceneIdByUserAndScenario(userId, scenarioId);

            if (lastSceneId == null) {
                lastSceneId = 1L; // 기본 시작 Scene ID
            }

            response.put("lastSceneId", lastSceneId);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/scenarios/{scenarioId}/scenes/{currentId}")
    public ResponseEntity<ApiResponse<UserScene>> getScene(
            @PathVariable Long scenarioId,
            @PathVariable Long currentId) {

        UserScene userScene = sceneService.getSceneDto(scenarioId, currentId);
        if (userScene == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, userScene));
    }

    @PostMapping("/scenarios/{scenarioId}/scenes/{currentId}")
    public ResponseEntity<Void> startScenario(
            @PathVariable Long scenarioId,
            @PathVariable Long currentId,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        try {
            String role = body.get("role");

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            String email = extractEmail(oAuth2User, registrationId);

            if (email == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            sceneService.insertScenarioPlayIfNotExists(email, scenarioId, currentId, role);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace(); // 이 부분이 반드시 로그에 찍히는지 확인
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/choose")
    public ResponseEntity<?> handleChoice(
            @RequestBody ChoiceRequest request,
            Authentication authentication) {

        ChoiceRequest.Effect stats = request.getEffect();
        List<ChoiceRequest.Item> items = request.getItem();
        List<String> itemNames = new ArrayList<>();

        if (items != null) {
            for (ChoiceRequest.Item item : items) {
                itemNames.add(item.getName());
            }
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String email = extractEmail(oAuth2User, registrationId);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Long user_id = userService.findIdByEmail(email);
        Long id = choiceService.findActiveScenarioPlayIdByUserId(user_id);

        Long currentSceneId = choiceService.findCurrentSceneIdByUserId(user_id);

        // 첫 시작
        if(currentSceneId == 1L) {
            // 직업 선택 시 다른 테이블도 생성 (아이템 이름 리스트 전달)
            choiceService.insertScenarioItemAndScenarioPlayStatIfNotExists(id, stats, itemNames);
        }
        // 테이블이 이미 있어서 update
        choiceService.updateCurrentScenarioIdById(id, currentSceneId);

        return ResponseEntity.ok().build();
    }
}
