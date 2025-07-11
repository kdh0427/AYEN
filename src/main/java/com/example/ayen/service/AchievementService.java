package com.example.ayen.service;

import com.example.ayen.dto.entity.UserAchievement;
import com.example.ayen.repository.AchievementRepository;
import com.example.ayen.repository.UserAchievementRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AchievementService {
    private final AchievementRepository aRepository;
    private final UserAchievementRepository useraRepository;

    public AchievementService(AchievementRepository aRepository, UserAchievementRepository useraRepository) {
        this.aRepository = aRepository;
        this.useraRepository = useraRepository;
    }

    public List<UserAchievement> findByUser_Id(Long userId){
        return useraRepository.findByUser_Id(userId);
    }

    public Boolean findByUser_idAndAchievement_Id(Long userId, Long achievementId) {
        return useraRepository.existsByUser_IdAndAchievement_Id(userId, achievementId);
    }
}
