package com.example.ayen.repository;

import com.example.ayen.dto.entity.ScenarioItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ScenarioItemRepository extends CrudRepository<ScenarioItem, Long> {

}
