package com.example.ayen.repository;

import com.example.ayen.dto.entity.Ending;
import com.example.ayen.dto.entity.UserEnding;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserEndingRepository extends CrudRepository<UserEnding, Long> {
    @Query(
            value = """
        SELECT ue.* 
        FROM user_ending ue
        JOIN (
            SELECT ending_id, MIN(id) AS min_id
            FROM user_ending
            WHERE user_id = :userId
            GROUP BY ending_id
        ) grouped ON ue.id = grouped.min_id
        """,
            nativeQuery = true
    )
    List<UserEnding> findDistinctFirstByUserId(Long userId);

    Boolean existsByUserIdAndEndingId(Long userId, Long endingId);
    Integer countByUserIdAndEndingId(Long userId, Long endingId);
}
