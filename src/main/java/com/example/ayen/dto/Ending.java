package com.example.ayen.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`Ending`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Ending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "ending_type")
    private EndingType endingType;
}
