package com.example.ayen.service;

import com.example.ayen.dto.entity.Choice;
import com.example.ayen.repository.ChoiceRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChoiceService {

    private final ChoiceRepository choiceRepository;

    public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    public List<Choice> findByScene_Id(Long sceneId) {
        return choiceRepository.findByScene_Id(sceneId);
    }
}
