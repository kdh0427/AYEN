package com.example.ayen.dto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Achievement")
@Getter
@Setter
@NoArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "`condition`", columnDefinition = "TEXT")
    private String condition;

    private String image_url;

    private int exp;

    public Achievement(String title, String description, String condition, String image_url, int exp) {
        this.title = title;
        this.description = description;
        this.condition = condition;
        this.image_url = image_url;
        this.exp = exp;
    }
}
