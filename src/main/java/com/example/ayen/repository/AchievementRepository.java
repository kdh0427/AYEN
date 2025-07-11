package com.example.ayen.repository;

import com.example.ayen.dto.entity.Achievement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    Optional<Achievement> findById(long id);
    @Query(value = "SELECT * FROM achievement WHERE JSON_UNQUOTE(JSON_EXTRACT(`condition`, '$.role')) = :role AND JSON_UNQUOTE(JSON_EXTRACT(`condition`, '$.ending_type')) = :endingType LIMIT 1", nativeQuery = true)
    Achievement findByConditionValues(@Param("role") String role, @Param("endingType") String endingType);
}
