package com.example.ayen.repository;

import com.example.ayen.dto.entity.Scenario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ScenarioRepository extends CrudRepository<Scenario, Long> {
    Optional<Scenario> findById(Long id);
}
