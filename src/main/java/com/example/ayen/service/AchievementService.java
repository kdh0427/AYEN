package com.example.ayen.service;

import com.example.ayen.dto.entity.UserAchievement;
import com.example.ayen.repository.AchievementRepository;
import com.example.ayen.repository.UserAchievementRepository;
import org.springframework.stereotype.Component;

import java.util.List;

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

}
