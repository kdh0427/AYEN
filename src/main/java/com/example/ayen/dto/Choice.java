package com.example.ayen.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`Choice`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scene_id")
    private Scene scene;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "next_scene_id")
    private Scene nextScene;

    @Column(name = "required_condition", columnDefinition = "JSON")
    private String requiredCondition;

    @Column(columnDefinition = "JSON")
    private String effect;
}
