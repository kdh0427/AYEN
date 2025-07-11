package com.example.ayen.repository;

import com.example.ayen.dto.entity.User;
import com.example.ayen.dto.entity.UserAchievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserAchievementRepository extends CrudRepository<UserAchievement, Long> {
    List<UserAchievement> findByUser_Id(Long userId);
    boolean existsByUser_IdAndAchievement_Id(Long userId, Long achievementId);
    List<UserAchievement> findByUserAndCheckedFalse(User user);
    Optional<UserAchievement> findByUser_IdAndAchievement_Id(Long userId, Long achievementId);
}
