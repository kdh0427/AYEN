package com.example.ayen.controller;

import com.example.ayen.dto.entity.Scenario;
import com.example.ayen.service.ScenarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScenarioController {
    private final ScenarioService scenarioService;

    public ScenarioController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @GetMapping("/scenarios")
    public Iterable<Scenario> findScenarios() {
        return scenarioService.getAllScenarios();
    }
}
