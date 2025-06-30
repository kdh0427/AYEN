package com.example.ayen.repository;

import com.example.ayen.dto.entity.ScenarioPlay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ScenarioPlayRepository extends CrudRepository<ScenarioPlay, Long> {
    Optional<ScenarioPlay> findByUser_EmailAndScenario_Id(String email, Long scenarioId);
}
