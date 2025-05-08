package com.example.ayen.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`Achievement`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "JSON")
    private String condition;
}
