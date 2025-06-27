package com.example.ayen.repository;

import com.example.ayen.dto.entity.ScenarioPlayStat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ScenarioPlayStatRepository extends CrudRepository<ScenarioPlayStat, Long> {

}
