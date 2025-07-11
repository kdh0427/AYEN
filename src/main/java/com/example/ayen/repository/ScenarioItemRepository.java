package com.example.ayen.repository;

import com.example.ayen.dto.entity.Item;
import com.example.ayen.dto.entity.ScenarioItem;
import com.example.ayen.dto.entity.ScenarioPlay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ScenarioItemRepository extends CrudRepository<ScenarioItem, Long> {
    Optional<ScenarioItem> findByScenarioPlayAndItem(ScenarioPlay scenarioPlay, Item item);
    List<ScenarioItem> findByScenarioPlayId(Long scenarioPlayId);

}
