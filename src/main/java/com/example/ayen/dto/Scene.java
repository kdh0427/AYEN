package com.example.ayen.dto;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "is_ending")
    private Boolean isEnding;
}
