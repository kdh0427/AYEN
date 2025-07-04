package com.example.ayen.repository;

import com.example.ayen.dto.entity.ScenarioPlay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ScenarioPlayRepository extends CrudRepository<ScenarioPlay, Long> {
    @Query("SELECT sp.scene.id FROM ScenarioPlay sp WHERE sp.user.id = :userId AND sp.scenario.id = :scenarioid And sp.is_finished = false ORDER BY sp.id DESC")
    Long findCurrentSceneIdByUserIdAndScenarioIdAndIs_finished(@Param("userId") Long userId, @Param("scenarioid") Long scenarioId);

    @Query("SELECT sp.scene.id FROM ScenarioPlay sp WHERE sp.user.id = :userId")
    Long findCurrentSceneIdByUserId(@Param("userId") Long userId);

    @Query("SELECT sp.id FROM ScenarioPlay sp WHERE sp.user.id = :userId AND sp.is_finished = false")
    Long findActiveScenarioPlayIdByUserId(@Param("userId") Long userId);

    @Query("SELECT sp.scenario.id FROM ScenarioPlay sp WHERE sp.user.id = :userId AND sp.is_finished = false")
    Long findScenarioIdByUserId(@Param("userId") Long userId);

    Optional<ScenarioPlay> findByUser_IdAndScenario_Id(Long userId, Long scenarioId);
    Optional<ScenarioPlay> findById(Long Id);
}
