package com.example.ayen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AchievementDetailResponse {
    private String title;
    private String image_url;
    private String description;
    private LocalDateTime achieved_at;
    private int exp;

    public AchievementDetailResponse(String title, String image_url, String description, LocalDateTime achievedAt, int exp) {
        this.title = title;
        this.image_url = image_url;
        this.description = description;
        this.achieved_at = achievedAt;
        this.exp = exp;
    }
}
