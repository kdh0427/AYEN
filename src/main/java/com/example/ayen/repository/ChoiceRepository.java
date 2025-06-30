package com.example.ayen.repository;

import com.example.ayen.dto.entity.Choice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ChoiceRepository extends CrudRepository<Choice, Long> {
    List<Choice> findByScene_Id(Long sceneId);
}
