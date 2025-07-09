package com.example.ayen.service;

import com.example.ayen.dto.entity.*;
import com.example.ayen.dto.response.UserScene;
import com.example.ayen.repository.*;
import com.example.ayen.dto.response.UserScene;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SceneService {

    private final ScenarioPlayRepository scenarioPlayRepository;
    private final SceneRepository sceneRepository;
    private final ChoiceRepository choiceRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final ScenarioRepository scenarioRepository;

    public SceneService(ScenarioPlayRepository scenarioPlayRepository, SceneRepository sceneRepository, ChoiceRepository choiceRepository, ObjectMapper objectMapper
    , UserRepository userRepository, ScenarioRepository scenarioRepository) {
        this.scenarioPlayRepository = scenarioPlayRepository;
        this.sceneRepository = sceneRepository;
        this.choiceRepository = choiceRepository;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.scenarioRepository = scenarioRepository;
    }

    public Long findLastSceneIdByUserAndScenario(Long userId, Long scenarioId) {
        return scenarioPlayRepository.findCurrentSceneIdByUserIdAndScenarioIdAndIs_finished(userId, scenarioId);
    }

    public UserScene getSceneDto(Long scenarioId, Long sceneId) {
        Scene scene = sceneRepository.findByScenario_IdAndId(scenarioId, sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        UserScene dto = new UserScene();
        dto.setContent(scene.getContent());
        dto.setImageUrl(scene.getImageUrl());
        dto.setEnding(scene.getIsEnding());

        List<Choice> choices = choiceRepository.findByScene_Id(scene.getId());
        List<UserScene.ChoiceDto> choiceDtos = new ArrayList<>();

        for (Choice c : choices) {
            UserScene.ChoiceDto cd = new UserScene.ChoiceDto();
            cd.setNextSceneId(c.getNextScene().getId());
            cd.setDescription(c.getDescription());

            // JSON 문자열을 EffectDto로 변환
            try {
                UserScene.EffectDto effectDto = objectMapper.readValue(
                        c.getEffect(), UserScene.EffectDto.class);
                cd.setEffect(effectDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse effect JSON", e);
            }

            choiceDtos.add(cd);
        }

        dto.setChoices(choiceDtos);
        return dto;
    }

    public void insertScenarioPlayIfNotExists(String email, Long scenarioId, Long currentId, String role) {
        Long userid = userRepository.findIdByEmail(email);

        if (userid == null) {
            throw new RuntimeException("User ID not found for email: " + email);
        }

        Optional<ScenarioPlay> existing = scenarioPlayRepository.findByUser_IdAndScenario_IdAndIsFinishedFalse(userid, scenarioId);

        if (existing.isEmpty()) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Scenario scenario = scenarioRepository.findById(scenarioId)
                    .orElseThrow(() -> new RuntimeException("Scenario not found"));

            Scene scene = sceneRepository.findById(currentId)
                    .orElseThrow(() -> new RuntimeException("Scene not found"));

            ScenarioPlay newPlay = new ScenarioPlay(user, scenario, scene, role);
            scenarioPlayRepository.save(newPlay);

            int cnt = user.getScenario_play_count();
            user.setScenario_play_count(++cnt);
            userRepository.save(user);
        }
    }
}
