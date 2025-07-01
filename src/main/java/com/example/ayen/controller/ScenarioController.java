package com.example.ayen.controller;

import com.example.ayen.dto.entity.Scenario;
import com.example.ayen.dto.response.ApiResponse;
import com.example.ayen.dto.response.ChoiceRequest;
import com.example.ayen.dto.response.UserScene;
import com.example.ayen.service.ScenarioService;
import com.example.ayen.service.SceneService;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class ScenarioController {
    private final SceneService sceneService;
    private final ScenarioService scenarioService;

    public ScenarioController(SceneService sceneService, ScenarioService scenarioService) {
        this.sceneService = sceneService;
        this.scenarioService = scenarioService;
    }

    @GetMapping("/scenarios")
    public Iterable<Scenario> findScenarios() {
        return scenarioService.getAllScenarios();
    }

    @PostMapping("/scenarios/{scenarioId}/scenes")
    public ResponseEntity<Map<String, Object>> getLastSceneId(
            @PathVariable Long scenarioId,
            OAuth2AuthenticationToken authentication) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 카카오 사용자 ID 추출
            Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
            String kakaoId = String.valueOf(attributes.get("id"));  // Long 형태일 수 있어 String 변환

            System.out.println("Kakao ID = " + kakaoId);

            // 해당 kakaoId로 마지막 sceneId 조회
            Long lastSceneId = sceneService.findLastSceneIdByUserAndScenario(Long.parseLong(kakaoId), scenarioId);
            if (lastSceneId == null) {
                lastSceneId = 1L;
            }

            response.put("lastSceneId", lastSceneId);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ex.printStackTrace(); // 디버깅용 로그
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
            @RequestParam String role,
            OAuth2AuthenticationToken authentication) {

        try {
            Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
            Long kakaoId = Long.parseLong(String.valueOf(attributes.get("id")));

            sceneService.insertScenarioPlayIfNotExists(kakaoId, scenarioId, currentId, role);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/choose")
    public ResponseEntity<?> handleChoice(
            @RequestBody ChoiceRequest request,
            OAuth2AuthenticationToken authentication) {

        Long kakaoId = Long.parseLong(String.valueOf(authentication.getPrincipal().getAttributes().get("id")));

        ChoiceRequest.Effect stats = request.getEffect();
        List<ChoiceRequest.Item> items = request.getItem_changes();

        // 사용 예시
        System.out.println("공격력: " + stats.getAttack());
        System.out.println("받은 아이템 수: " + items.size());

        return ResponseEntity.ok().build();
    }
}
