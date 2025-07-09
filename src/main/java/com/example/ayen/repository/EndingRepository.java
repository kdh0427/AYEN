package com.example.ayen.repository;

import com.example.ayen.dto.entity.Ending;
import com.example.ayen.dto.entity.Scene;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface EndingRepository extends CrudRepository<Ending, Long> {
    Optional<Ending> findBySceneId(Long id);
}
