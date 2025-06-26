package com.example.ayen.service;

import com.example.ayen.dto.Achievement;
import com.example.ayen.repository.AchievementRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AchievementService {
    private final AchievementRepository aRepository;

    public AchievementService(AchievementRepository aRepository) {
        this.aRepository = aRepository;
    }

    public Optional<Achievement> findById(long id) {
        return aRepository.findById(id);
    }
}
