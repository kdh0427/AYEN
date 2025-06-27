package com.example.ayen.repository;

import com.example.ayen.dto.entity.ScenarioPlay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ScenarioPlayRepository extends CrudRepository<ScenarioPlay, Long> {

}
