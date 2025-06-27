package com.example.ayen.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AchievementResponse {
    private String Title;
    private String Url;
    private LocalDateTime achievedAt;  // 업적 날짜 타입에 맞게

    public AchievementResponse(String Title, String Url, LocalDateTime achievedAt) {
        this.Title = Title;
        this.Url = Url;
        this.achievedAt = achievedAt;
    }

    // getters, setters 생략
}
