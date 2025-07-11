package com.example.ayen.dto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "`UserAchievement`")
@Getter
@Setter
@NoArgsConstructor
public class UserAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "achievement_id", foreignKey = @ForeignKey(name = "fk_userachievement_achievement"))
    private Achievement achievement;

    @Column(name = "achieved_at")
    private LocalDateTime achievedAt;

    private Boolean checked;

    public UserAchievement(User user, Achievement achievement) {
        this.user = user;
        this.achievement = achievement;
        this.achievedAt = LocalDateTime.now();
        this.checked = false;
    }
}
