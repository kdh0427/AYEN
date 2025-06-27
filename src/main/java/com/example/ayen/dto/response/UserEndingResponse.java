package com.example.ayen.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserEndingResponse {
    private Long id;
    private String Title;
    private String Url;
    private LocalDateTime achievedAt;

    public UserEndingResponse(Long id, String Title, String Url, LocalDateTime achievedAt) {
        this.id = id;
        this.Title = Title;
        this.Url = Url;
        this.achievedAt = achievedAt;
    }
}
