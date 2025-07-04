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
    @JoinColumn(name = "scenario_id") //
    private Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "current_scene_id")
    private Scene scene;

    private String chosen_role;

    private LocalDateTime started_at, ending_at;

    private boolean is_finished;

    private LocalDateTime updated_at;

    public ScenarioPlay(User user, Scenario scenario, Scene scene, String chosen_role) {
        this.user = user;
        this.scenario = scenario;
        this.scene = scene;
        this.chosen_role = chosen_role;
        this.started_at = LocalDateTime.now();
        this.is_finished = false;
        this.updated_at = LocalDateTime.now();
    }

}
