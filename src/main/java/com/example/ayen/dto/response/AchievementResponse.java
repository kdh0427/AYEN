package com.example.ayen.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AchievementResponse {
    private Long id;
    private String Title;
    private String Url;
    private LocalDateTime achieved_at;  // 업적 날짜 타입에 맞게

    public AchievementResponse(Long id, String Title, String Url, LocalDateTime achievedAt) {
        this.id = id;
        this.Title = Title;
        this.Url = Url;
        this.achieved_at = achievedAt;
    }

    // getters, setters 생략
}
