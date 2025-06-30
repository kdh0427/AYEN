package com.example.ayen.controller;

import com.example.ayen.dto.entity.Scenario;
import com.example.ayen.dto.response.UserScene;
import com.example.ayen.service.ScenarioService;
import com.example.ayen.service.SceneService;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
            @AuthenticationPrincipal UserDetails user) {

        System.out.println("user = " + user);
        System.out.println("username = " + user.getUsername());
        Map<String, Object> response = new HashMap<>();
        try {
            Long lastSceneId = sceneService.findLastSceneIdByUserAndScenario(user.getUsername(), scenarioId);
            if (lastSceneId == null) {
                lastSceneId = 1L;
            }

            response.put("lastSceneId", lastSceneId);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            // 여기서 실제 예외 메시지와 스택트레이스를 로그에 남깁니다.
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/scenarios/{scenarioId}/scenes/{currentId}")
    public ResponseEntity<UserScene> getScene(
            @PathVariable Long scenarioId,
            @PathVariable Long currentId) {

        UserScene userScene = sceneService.getSceneDto(scenarioId, currentId);
        if (userScene == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userScene);
    }

}
