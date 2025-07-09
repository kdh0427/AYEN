package com.example.ayen.repository;

import com.example.ayen.dto.entity.ScenarioPlay;
import com.example.ayen.dto.entity.ScenarioPlayStat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ScenarioPlayStatRepository extends CrudRepository<ScenarioPlayStat, Long> {

    Optional<ScenarioPlayStat> findByScenarioPlay(ScenarioPlay scenarioPlay);
    Optional<ScenarioPlayStat> findById(Long id);
}
