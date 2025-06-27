package com.example.ayen.repository;

import com.example.ayen.dto.entity.UserAchievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserAchievementRepository extends CrudRepository<UserAchievement, Long> {
    List<UserAchievement> findByUser_Id(Long userId);
}
