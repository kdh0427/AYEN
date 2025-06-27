package com.example.ayen.repository;

import com.example.ayen.dto.entity.Scene;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SceneRepository extends CrudRepository<Scene, Long> {
}
