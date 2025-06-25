package com.example.ayen.repository;

import com.example.ayen.dto.CharacterStat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CharacterStatRepository extends CrudRepository<CharacterStat, Long> {

}
