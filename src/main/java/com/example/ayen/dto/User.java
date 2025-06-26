package com.example.ayen.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Character> characters = new ArrayList<>();

    private String email, user_token, name;

    @Enumerated(EnumType.STRING)
    private SocialType social_type;

    public enum SocialType {
        GOOGLE,
        KAKAO,
        NAVER
    }
    private int level, exp, achievementCount, scenario_play_count;

    private LocalDateTime created_at;

    public User(String email, String user_token, String name, SocialType social_type, int level, int exp, int achievement_count, int scenario_play_count) {
        this.email = email;
        this.user_token = user_token;
        this.name = name;
        this.social_type = social_type;
        this.level = level;
        this.exp = exp;
        this.achievementCount = achievement_count;
        this.scenario_play_count = scenario_play_count;
        this.created_at = LocalDateTime.now();
    }
}
