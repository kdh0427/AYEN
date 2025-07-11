package com.example.ayen.service;

import com.example.ayen.dto.entity.Ending;
import com.example.ayen.dto.entity.UserEnding;
import com.example.ayen.repository.EndingRepository;
import com.example.ayen.repository.UserEndingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EndingService {
    private final UserEndingRepository uendingRepository;
    private final EndingRepository endingRepository;

    public EndingService(UserEndingRepository uendingRepository, EndingRepository endingRepository) {
        this.uendingRepository = uendingRepository;
        this.endingRepository = endingRepository;
    }

    public List<UserEnding> findByUser_Id(Long userId){
        return uendingRepository.findDistinctFirstByUserId(userId);
    }

    public Optional<Ending> findById(Long id){
        return endingRepository.findById(id);
    }

    public Integer countByUserIdAndEndingId(Long userId, Long endingId){
        return uendingRepository.countByUserIdAndEndingId(userId, endingId);
    }
}
