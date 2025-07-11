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
    private LocalDateTime achieved_at;

    public UserEndingResponse(Long id, String Title, String Url, LocalDateTime achieved_at) {
        this.id = id;
        this.Title = Title;
        this.Url = Url;
        this.achieved_at = achieved_at;
    }
}
