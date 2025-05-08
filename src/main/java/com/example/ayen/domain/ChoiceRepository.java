package com.example.ayen.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ChoiceRepository extends CrudRepository<Choice, Long> {
}
