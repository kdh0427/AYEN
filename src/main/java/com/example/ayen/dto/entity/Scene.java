package com.example.ayen.dto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "`Scene`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "is_ending")
    private Boolean isEnding;
}
