package com.example.ayen.repository;

import com.example.ayen.dto.Achievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    Optional<Achievement> findById(long id);
}
