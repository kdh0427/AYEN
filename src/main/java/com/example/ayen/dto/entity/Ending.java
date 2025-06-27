package com.example.ayen.dto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`Ending`")
@Getter @Setter @NoArgsConstructor
public class Ending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "sceane_id")
    private Scene scene;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image_url;

    @Enumerated(EnumType.STRING)
    @Column(name = "ending_type")
    private EndingType endingType;

    private int exp;

    public Ending(String title, String description,String image_url, EndingType endingType, int exp) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.endingType = endingType;
        this.exp = exp;
    }
}
