package com.example.ayen.repository;

import com.example.ayen.dto.entity.Scene;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface SceneRepository extends CrudRepository<Scene, Long> {
    Optional<Scene> findByScenario_IdAndId(Long scenarioId, Long id);

}
