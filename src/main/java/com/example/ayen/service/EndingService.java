package com.example.ayen.service;

import com.example.ayen.dto.entity.UserEnding;
import com.example.ayen.repository.UserEndingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EndingService {
    private final UserEndingRepository uendingRepository;

    public EndingService(UserEndingRepository uendingRepository) {
        this.uendingRepository = uendingRepository;
    }

    public List<UserEnding> findByUser_Id(Long userId){
        return uendingRepository.findByUser_Id(userId);
    }
}
