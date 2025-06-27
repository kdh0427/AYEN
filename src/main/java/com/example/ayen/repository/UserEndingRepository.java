package com.example.ayen.repository;

import com.example.ayen.dto.entity.Ending;
import com.example.ayen.dto.entity.UserEnding;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserEndingRepository extends CrudRepository<UserEnding, Long> {
    List<UserEnding> findByUser_Id(Long userId);
}
