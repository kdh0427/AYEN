package com.example.ayen.dto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "`ScenarioPlay`")
public class ScenarioPlay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "senario_id") //
    private Scenario senario;

    @OneToOne
    @JoinColumn(name = "current_scene_id")
    private Scene scene;

    private String chosen_role;

    private LocalDateTime started_at, ending_at;

    private boolean is_finished;

    private LocalDateTime updated_at;

    public ScenarioPlay(User user, Scenario senario, Scene scene, String chosen_role) {
        this.user = user;
        this.senario = senario;
        this.scene = scene;
        this.chosen_role = chosen_role;
        this.started_at = LocalDateTime.now();
        this.is_finished = false;
        this.updated_at = LocalDateTime.now();
    }

}
