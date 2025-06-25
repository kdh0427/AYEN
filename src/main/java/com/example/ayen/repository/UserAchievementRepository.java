package com.example.ayen.repository;

import com.example.ayen.dto.UserAchievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserAchievementRepository extends CrudRepository<UserAchievement, Long> {
}
