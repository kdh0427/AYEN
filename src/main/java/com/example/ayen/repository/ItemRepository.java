package com.example.ayen.repository;

import com.example.ayen.dto.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ItemRepository extends CrudRepository<Item, Long> {

}
