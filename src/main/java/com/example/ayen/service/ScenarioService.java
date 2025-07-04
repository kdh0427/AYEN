package com.example.ayen.service;

import com.example.ayen.dto.entity.Scenario;
import com.example.ayen.dto.entity.Scene;
import com.example.ayen.repository.ScenarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;

    public ScenarioService(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    public Iterable<Scenario> getAllScenarios() {
        return scenarioRepository.findAll();
    }

    public ResponseEntity<Scenario> getScenarioById(Long id) {
        Optional<Scenario> scenario = scenarioRepository.findById(id);
        if (scenario.isPresent()) {
            return ResponseEntity.ok(scenario.get());
        }
        return ResponseEntity.notFound().build();
    }
}
