package com.example.ayen.service;

import com.example.ayen.dto.entity.Choice;
import com.example.ayen.dto.entity.Scene;
import com.example.ayen.dto.response.UserScene;
import com.example.ayen.repository.ChoiceRepository;
import com.example.ayen.repository.ScenarioPlayRepository;
import com.example.ayen.dto.response.UserScene;
import com.example.ayen.repository.SceneRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SceneService {

    private final ScenarioPlayRepository scenarioPlayRepository;
    private final SceneRepository sceneRepository;
    private final ChoiceRepository choiceRepository;
    private final ObjectMapper objectMapper;

    public SceneService(ScenarioPlayRepository scenarioPlayRepository, SceneRepository sceneRepository, ChoiceRepository choiceRepository, ObjectMapper objectMapper) {
        this.scenarioPlayRepository = scenarioPlayRepository;
        this.sceneRepository = sceneRepository;
        this.choiceRepository = choiceRepository;
        this.objectMapper = objectMapper;
    }

    public Long findLastSceneIdByUserAndScenario(String username, Long scenarioId) {
        return scenarioPlayRepository.findByUser_EmailAndScenario_Id(username, scenarioId)
                .map(scenarioPlay -> scenarioPlay.getScene().getId())
                .orElse(null);
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
}
